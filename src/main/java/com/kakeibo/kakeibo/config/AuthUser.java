package com.kakeibo.kakeibo.config;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

/** JWT から取り出したログインユーザー情報 */
public record AuthUser(Integer id, String userName) {

    public static AuthUser current() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof AuthUser user) {
            return user;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "ログインが必要です");
    }

    /** リクエストで指定された userId がログインユーザー本人のものか検証し、本人の ID を返す */
    public static Integer requireSelf(Integer requestedUserId) {
        AuthUser user = current();
        if (requestedUserId != null && !requestedUserId.equals(user.id())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "他のユーザーのデータにはアクセスできません");
        }
        return user.id();
    }
}
