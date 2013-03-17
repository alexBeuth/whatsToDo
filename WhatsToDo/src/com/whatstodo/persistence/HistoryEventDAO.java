package com.whatstodo.persistence;

import java.util.Date;
import java.util.List;

import com.whatstodo.models.HistoryEvent;
import com.whatstodo.models.HistoryEvent.Type;

public interface HistoryEventDAO extends BaseDAO<HistoryEvent> {
	
	List<HistoryEvent> find(Type type, Date after, Long entityUid, Boolean isSynchronized);

}
