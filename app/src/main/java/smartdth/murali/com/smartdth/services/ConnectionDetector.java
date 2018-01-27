package smartdth.murali.com.smartdth.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {
	
	private Context _context;
	
	public ConnectionDetector(Context context){
		this._context = context;
	}
	
	public boolean isNetworkOn(Context context) { ConnectivityManager connMgr =
			(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}
}
