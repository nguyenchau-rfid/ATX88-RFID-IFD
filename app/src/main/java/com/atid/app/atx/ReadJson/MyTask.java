package com.atid.app.atx.ReadJson;


import java.util.TimerTask;

public class MyTask extends TimerTask {
    private TokenManager ngayToken;

    @Override
    public void run() {

        ngayToken = new TokenManager();
        ngayToken.sendTokenifd();
        ngayToken.sendTokenKiotViet();
        System.out.println("Hen Gio Lay Token" + ngayToken.getDateT()
                + " - " + ngayToken.Laytoken(TokenManager.getContext(), "Ngay") + " token " + ngayToken.Laytoken(TokenManager.getContext(), "keytoken"));

    }
}
