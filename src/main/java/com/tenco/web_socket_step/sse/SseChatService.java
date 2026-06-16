package com.tenco.web_socket_step.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.yaml.snakeyaml.emitter.Emitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SseChatService {

    private final SseChatRepository sseChatRepository;

    // 접속한 클라이언트를 보관할 자료 구조 -> ConcurrentHashMap 멀티 스레드에서 안전하다.
    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    // 1. 클라이언트가 접속하면 호출됨 (SSE 연결)
    public SseEmitter createConnection(String clientId) {
        // 파이프 라인 연결 - 유지 시간이 60초 권장
        SseEmitter emitter = new SseEmitter(60 * 1000L);

        // 보관
        emitterMap.put(clientId, emitter);

        // 필수 콜백 함수 등록 처리
        // 연결 종료 / 타임아웃 시 Map 목록에서 제거 처리 (불필요한 메모리 누수 방지)
        emitter.onCompletion(() -> emitterMap.remove(clientId));
        emitter.onTimeout(() -> emitterMap.remove(clientId));
        emitter.onError((e) -> emitterMap.remove(clientId));

        // 1000L : 서버와 클라이언트가 연결(파이브)을 유지하는 최대 시간 (현재 1초)
        // 1초 뒤에 : 서버가 클라이언 연결을 끊어 버리고 있다.
        // 참고: 0L로 설정을 하면 무한으로 계속 연결이 되지만 실무에서는 좀비 커네셕이 생성이
        // 되어서 실제로 0L로 하지 않는다. - 폴링 방식이 절대 아님
        try {
            emitter.send(SseEmitter.event().name("connect").data("연결됨"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return emitter;
    }

    // 2. DB 에 저장된 채팅 글 조회 기능
    public List<SseChat> findAll() {
        return sseChatRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    // 3. 메세지 DB 저장 및 전파(브로드캐스트 처리)
    @Transactional // insert 가 일어나기 때문에
    public void addMessage(String message, String sender) {

        SseChat sseChat = SseChat.builder()
                .message(message)
                .sender(sender)
                .build();

        sseChatRepository.save(sseChat);
        // 브로드 캐스트 처리
        broadcast(message, sender);
    }

    private void broadcast(String message, String sender) {
        // 프로토콜 {json형식},-->   보낸사람 : 메시지
        String formattedMessage = sender + ": " + message; // (반드시 공백 한칸)

        emitterMap.forEach((id, sseEmitter) ->{
            try {
                sseEmitter.send(SseEmitter
                        .event()
                        .name("newMessage")
                        .data(formattedMessage));
            } catch (IOException e) {
                // 전송 실패 시 map 에서 제거 처리
                emitterMap.remove(id);
            }
        });
    }
}
