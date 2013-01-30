package com.mobien.webservices;

import java.net.SocketTimeoutException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.*;

import com.mobien.demoapp.R;

import android.content.Context;
import android.util.Log;


@SuppressWarnings("deprecation")
public class HttpEngine{
	private final String NAMESPACE = "http://tempuri.org/";
	int timeout = 6*60000;
	public HttpEngine() {

	}

	public String[][] webRequest(String method, Context mContext,
			String[][] inparams, String outParams[]) throws Exception {
		
		final String URL = mContext.getString(R.string.web_service_url);
		
		String[][] outParamWithValue = new String[outParams.length][2];
		try {
			String action = NAMESPACE + method;
			SoapObject request = new SoapObject(NAMESPACE, method);			
			// SoapObject
			
			if (inparams != null) {
				for (int i = 0; i < inparams.length; i++) {
					request.addProperty(inparams[i][0], inparams[i][1]);
				}
			}
			Log.i("http engine", "request formed");
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			
			// envelope.bodyOut = request;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("http engine", "envelope formed");
			AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(
					URL, timeout);
			androidHttpTransport.call(action, envelope);
			Log.i("http engine", "web service called");
			SoapObject response = (SoapObject) envelope.bodyIn;
			if (response.getPropertyCount() > 0) {
				for (int i = 0; i < outParams.length; i++) {
					try {
						outParamWithValue[i][0] = outParams[i];
						outParamWithValue[i][1] = response.getProperty(outParams[i]).toString();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				}
			}
			
			System.out.println(response.toString());
			/*
			 * Log.i("response",response.toString()); int startIn =
			 * response.toString().indexOf("=<"); int endIn =
			 * response.toString().indexOf('}'); reply =
			 * response.toString().substring(startIn+1, endIn-1);
			 */
		}
		catch (SoapFault e) {
			outParamWithValue[0][1] = "SOAP";
			outParamWithValue[0][0] = "Soap Fault. Please try after sometime.";
			return outParamWithValue;
		}
		catch (SocketTimeoutException e) {
			// TODO: handle exception
			outParamWithValue[0][1] = "SOCKET";
			outParamWithValue[0][0] = "Socket timeout. Please try after sometime.";
			return outParamWithValue;
		}catch (Exception ex) {
			//FileUtility.captureLog(ex.getMessage());
			Log.i("exception", ex.toString());
			throw ex;
		}
		return outParamWithValue;
	}

}

