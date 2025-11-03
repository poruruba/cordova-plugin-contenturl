package jp.or.sample.ContentUrlPlugin;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import android.net.Uri;
import java.util.Base64;

public class Main extends CordovaPlugin {
	public static String TAG = "ContentUrlPlugin.Main";

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView)
	{
		Log.d(TAG, "[Plugin] initialize called");
		super.initialize(cordova, webView);
	}

	@Override
	public void onResume(boolean multitasking)
	{
		Log.d(TAG, "[Plugin] onResume called");
		super.onResume(multitasking);
	}

	@Override
	public void onPause(boolean multitasking)
	{
		Log.d(TAG, "[Plugin] onPause called");
		super.onPause(multitasking);
	}

	@Override
	public void onNewIntent(Intent intent)
	{
		Log.d(TAG, "[Plugin] onNewIntent called");
		super.onNewIntent(intent);
	}

	private void sendMessageToJs(JSONObject message, CallbackContext callback) {
		final PluginResult result = new PluginResult(PluginResult.Status.OK, message);
		result.setKeepCallback(true);
		if( callback != null )
			callback.sendPluginResult(result);
	}
	
	public static JSONArray toIntJSONArray( byte[] barray, int offset, int len )
	{
		JSONArray jarray = new JSONArray();
		for( int i = 0 ; i < len ; i++ )
			jarray.put(barray[offset + i] & 0x00ff);

		return jarray;
	}
	
	public static byte[] base64decode(String encoded){
		return Base64.getDecoder().decode(encoded);
	}
	
	public static String base64encode(byte[] input){
		return Base64.getEncoder().encodeToString(input);
	}

	@Override
	public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException
	{
		Log.d(TAG, "[Plugin] execute called");
		if( action.equals("readContent") ){
			try{
				String contentUrl = args.getString(0);
				Uri uri = Uri.parse(contentUrl);

				InputStream inputStream = cordova.getActivity().getContentResolver().openInputStream(uri);
				if (inputStream == null) {
				    throw new Exception("Unable to open InputStream for URI: " + uri);
				}

				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				byte[] data = new byte[4096];
				int bytesRead;
				while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
				    buffer.write(data, 0, bytesRead);
				}

				inputStream.close();
				byte[] barray = buffer.toByteArray();
				String mimeType = cordova.getActivity().getContentResolver().getType(uri);


				JSONObject result = new JSONObject();
				result.put("buffer", base64encode(barray));
				result.put("mimeType", mimeType);
				callbackContext.success(result);
			} catch (Exception ex) {
				callbackContext.error(ex.getMessage());
			}      
		}else
			
	    {
			String message = "Unknown action : (" + action + ") " + args.getString(0);
			Log.d(TAG, message);
			callbackContext.error(message);
			return false;
		}

		return true;
	}
}
