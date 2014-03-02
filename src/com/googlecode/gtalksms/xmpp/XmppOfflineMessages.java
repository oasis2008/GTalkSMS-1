package com.googlecode.gtalksms.xmpp;

import java.util.Iterator;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.OfflineMessageManager;

import android.content.Context;

import com.googlecode.gtalksms.tools.Log;
import com.googlecode.gtalksms.tools.Tools;

public class XmppOfflineMessages {

	public static void handleOfflineMessages(XMPPConnection connection, String[] notifiedAddresses, Context ctx)
	        throws XMPPException {
		Log.i("Begin retrieval of offline messages from server");
		OfflineMessageManager offlineMessageManager = new OfflineMessageManager(connection);

		if (!offlineMessageManager.supportsFlexibleRetrieval()) {
            Log.d("Offline messages not supported");
            return;
        }

		if (offlineMessageManager.getMessageCount() == 0) {
			Log.d("No offline messages found on server");
		} else {
            Iterator<Message> i = offlineMessageManager.getMessages();
            while (i.hasNext()) {
                Message msg = i.next();
                String fullJid = msg.getFrom();
                String bareJid = StringUtils.parseBareAddress(fullJid);
                String messageBody = msg.getBody();
                Log.d("Retrieved offline message from " + fullJid + " with content: " + (messageBody.length() > 40 ? messageBody.substring(0, 40) : messageBody));
                for (String notifiedAddress : notifiedAddresses) {
                    if (bareJid.equals(notifiedAddress) && (messageBody != null)) {
                        Tools.startSvcXMPPMsg(ctx, messageBody, fullJid);
                    }
                }
            }
            offlineMessageManager.deleteMessages();
        }
		Log.i("End of retrieval of offline messages from server");
	}
}
