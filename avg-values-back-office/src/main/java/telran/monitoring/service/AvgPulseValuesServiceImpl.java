package telran.monitoring.service;

import java.time.LocalDateTime;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import static telran.monitoring.entities.AvgPulseDoc.*;

import telran.monitoring.entities.AvgPulseDoc;

@Service
public class AvgPulseValuesServiceImpl implements AvgPulseValuesService{
	private static final String AVG_VALUE = "avgValue";
	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public int getAvgPulseValue(long patientId) {
		MatchOperation matchOperation = match(Criteria.where(PATIENT_ID).is(patientId));
		
		return avgValueRequest(matchOperation);
	}

	private int avgValueRequest(MatchOperation matchOperation) {
		GroupOperation groupOperation = group().avg(PULSE_VALUE).as(AVG_VALUE);
		Aggregation pipeline = newAggregation(matchOperation, groupOperation);
		Document document = mongoTemplate.aggregate(pipeline, AvgPulseDoc.class, Document.class)
				.getUniqueMappedResult();
		return document == null ? 0 : document.getDouble(AVG_VALUE).intValue();
	}

	@Override
	public int getAvgPulseValueDates(long patientId, LocalDateTime from, LocalDateTime to) {
		MatchOperation match = match(Criteria.where(PATIENT_ID).is(patientId)
				.andOperator(Criteria.where(DATE_TIME).gte(from).lte(to)));
		return avgValueRequest(match);
	}

}