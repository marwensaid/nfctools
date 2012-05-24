package org.nfctools.test;

import java.util.Collection;

import org.nfctools.ndef.NdefListener;
import org.nfctools.ndef.Record;
import org.nfctools.snep.GetResponseListener;
import org.nfctools.snep.SnepAgent;
import org.nfctools.snep.Sneplet;

public class NotifyingNdefListener implements NdefListener, Sneplet, GetResponseListener {

	private Object objectToNotify;
	private Collection<Record> records;
	private Collection<Record> getResponseRecords;
	private Collection<Record> receivedGetResponseRecords;

	public NotifyingNdefListener(Object objectToNotify) {
		this.objectToNotify = objectToNotify;
	}

	@Override
	public void onNdefMessages(Collection<Record> records) {
		this.records = records;
		synchronized (objectToNotify) {
			objectToNotify.notify();
		}
	}

	public Collection<Record> getRecords() {
		return records;
	}

	public void setGetResponseRecords(Collection<Record> getResponseRecords) {
		this.getResponseRecords = getResponseRecords;
	}

	@Override
	public Collection<Record> doGet(Collection<Record> requestRecords) {
		return getResponseRecords;
	}

	@Override
	public void doPut(Collection<Record> requestRecords) {
		this.records = requestRecords;
		synchronized (objectToNotify) {
			objectToNotify.notify();
		}
	}

	@Override
	public void onDisconnect() {
	}

	@Override
	public void onGetResponse(Collection<Record> records, SnepAgent snepAgent) {
		receivedGetResponseRecords = records;
		synchronized (objectToNotify) {
			objectToNotify.notify();
		}
	}

	public Collection<Record> getReceivedGetResponseRecords() {
		return receivedGetResponseRecords;
	}

}
