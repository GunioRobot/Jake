package com.jakeapp.core.domain;

import javax.persistence.Entity;
import java.util.UUID;
import java.util.Date;
import java.io.Serializable;

@Entity
public class TagLogEntry extends LogEntry<Tag> implements Serializable {
    private static final long serialVersionUID = -7799185912611559431L;

    public TagLogEntry(LogAction logAction,Tag belongsTo,
                       UserId member) {
		super(UUID.randomUUID(), logAction, getTime(),
				belongsTo, member, null, null, true);
        if (logAction != LogAction.TAG_ADD && logAction != LogAction.TAG_REMOVE)
			throw new IllegalArgumentException("invalid logaction for logentry");
        if(belongsTo.getObject() != null && belongsTo.getObject().getUuid() != null)
        	this.setObjectuuid(belongsTo.getObject().getUuid().toString());
    }

    public TagLogEntry() {
    }
}
