import rx.Observable;
import rx.Subscriber;


public class PriceObserveable{
	
	public Observable<String> get(){
		return Observable.create(new Observable.OnSubscribe<String>() {

			@Override
			public void call(Subscriber<? super String> sub) {
				for(int i = 0; i < 10; i++){
					sub.onNext("pushed val: " + i);
				}
				
				if(!sub.isUnsubscribed())
					sub.onCompleted();
			}
		});
	}
}
