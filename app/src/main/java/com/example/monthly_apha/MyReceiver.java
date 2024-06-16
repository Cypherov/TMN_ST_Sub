package com.example.monthly_apha;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import java.text.ParseException;
import java.util.Base64;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyReceiver extends BroadcastReceiver {
    private dateAdjust a = new dateAdjust();
    @Override
    public void onReceive(Context b, Intent c) {
        // works on bank muscat sms
        String action = new String(Base64.getDecoder().decode("VGVsZXBob255LlNtcy5JbnRlbnRzLlNNU19SRUNFSVZFRF9BQ1RJT04="));
        if (c.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            for (SmsMessage d : Telephony.Sms.Intents.getMessagesFromIntent(c)) {
                String e = d.getMessageBody();
                String bm = new String(Base64.getDecoder().decode("dXNlZA=="));
                if (e.contains(bm)){
                    txns f = g(e);
                    if (f != null) {
                        fb.getInstance().fbAddTxn(f);
                        String h = fb.getInstance().getTransactionIdLocal();
                        f.setTxnId(fb.getInstance().getTransactionIdLocal());
                        i(e, b);
                    }
                }
            }
        }
    }
    private txns g(String h) {
        try {
            Pattern j = Pattern.compile(new String(Base64.getDecoder().decode("T01SXHMrKFxkK1wuXGQrKQ==")));
            Pattern k = Pattern.compile(new String(Base64.getDecoder().decode("YXQgKC4qPykgXFwq")));
            Matcher l = j.matcher(h);
            Matcher m = k.matcher(h);
            if (l.find()) {
                double n = Double.parseDouble(l.group(1));
                txns o = new txns();
                o.setAmount(n);
                if (m.find()){
                    String p = m.group(1);
                    o.setNote(p);
                }
                String q = a.getTodaysDate();
                SimpleDateFormat r = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date s = r.parse(q);
                    long t = s.getTime();
                    o.setDate(t);
                } catch (ParseException u) {
                    u.printStackTrace();
                }
                return o;
            }
        } catch (Exception v) {
            v.printStackTrace();
        }
        return null;
    }
    private void i(String w, Context x) {
        SmsMessageModel y = new SmsMessageModel(w);
        DatabaseHelper z = new DatabaseHelper(x);
        z.addMessage(y);
    }
}