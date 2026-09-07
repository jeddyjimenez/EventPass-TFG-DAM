package com.example.eventos_mobile;

public class ValidacionRequest {
    private String qrToken;
    private Long idEvento;

    public ValidacionRequest(String qrToken, Long idEvento) {
        this.qrToken = qrToken;
        this.idEvento = idEvento;
    }

    public String getQrToken() { return qrToken; }
    public Long getIdEvento() { return idEvento; }
}
