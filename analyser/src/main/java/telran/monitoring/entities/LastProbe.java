package telran.monitoring.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash
public class LastProbe {
	@Id
	long patientId;
	int lastValue;
	public LastProbe(long patientId, int lastValue) {
		this.patientId = patientId;
		this.lastValue = lastValue;
	}
	public LastProbe() {
		
	}
	public int getLastValue() {
		return lastValue;
	}
	public void setLastValue(int lastValue) {
		this.lastValue = lastValue;
	}
	public long getPatientId() {
		return patientId;
	}
	

}
