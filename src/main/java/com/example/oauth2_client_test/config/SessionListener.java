package com.example.oauth2_client_test.config;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class SessionListener implements HttpSessionListener {
    // 인메모리 세션 저장소에서, 세션 목록을 조회하기 위한 확인용 클래스

    private final Set<String> activeSessions = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        activeSessions.add(se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        activeSessions.remove(se.getSession().getId());
    }

    public Set<String> getActiveSessions() {
        return activeSessions;
    }
}
