package com.gold.kiwi.common.ad.close;

import com.gold.kiwi.common.ad.bean.AdBean;
import com.gold.kiwi.common.ad.listener.AdInterface;
import com.gold.kiwi.common.ad.listener.AdRetryListener;

public interface AdCloseInterface extends AdInterface
{
	void setAdBean(AdBean bean);
	void setAdRetryListener(AdRetryListener listener);
	void closeRequest(int index);
	void closeStartAd();
	void closeStopAd();
}
