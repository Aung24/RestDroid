package com.nautilusapps.RestDroid;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;


public class ProgressMultipartEntity extends MultipartEntity {

	private ProgressDelegate delegate;

	public ProgressMultipartEntity(HttpMultipartMode mode, ProgressDelegate delegate) {
		super(mode);
		this.delegate = delegate;
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		super.writeTo(new ProgressOutputStream(outstream, delegate, getContentLength()));
	}

	public static class ProgressOutputStream extends FilterOutputStream {

		private ProgressDelegate delegate;
		private long transferred;
		private long contentLength;
		private int delegateDelay;

		public ProgressOutputStream(OutputStream os, ProgressDelegate delegate, long contentLength) {
			super(os);
			this.delegate = delegate;
			this.transferred = 0l;
			this.contentLength = contentLength;
			this.delegateDelay = 0;
		}
	
		private static final int DELAY_SIZE = 1024;
		private void updateDelegate() throws IOException {
			if (delegateDelay > DELAY_SIZE) {
				delegateDelay = 0;
				int percent = (int)(100.0 * ((float)transferred / (float)contentLength));
				delegate.updateProgress(percent);
			}
			if (delegate.hasCanceled())
				throw new IOException("Upload Canceled");
		}

		@Override
		public void write(byte[] buffer, int offset, int length) throws IOException {
			out.write(buffer, offset, length);
			transferred += length;
			delegateDelay += length;
			updateDelegate();
		}
		
		@Override
		public void write(int oneByte) throws IOException {
			out.write(oneByte);
			transferred++;
			delegateDelay++;
			updateDelegate();
		}

	}

}
