package com.kakeibo.kakeibo.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API のバージョンを返す（認証不要）。
 * フロントエンドはこれを見て、サーバーが古いままデプロイされていないかを確認する。
 * API の互換性が変わる変更を入れたら API_VERSION を上げ、フロントの REQUIRED_API_VERSION も合わせる。
 */
@RestController
public class VersionController {

    public static final int API_VERSION = 5;

    @GetMapping("/api/version")
    public Map<String, Integer> version() {
        return Map.of("apiVersion", API_VERSION);
    }
}
