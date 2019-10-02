package org.openl.itest.serviceclass;

import org.openl.rules.context.IRulesRuntimeContext;
import org.openl.rules.ruleservice.storelogdata.cassandra.annotation.StoreLogDataToCassandra;

public interface Simple2ServiceAnnotationTemplate {

    @StoreLogDataToCassandra
    String Hello(IRulesRuntimeContext runtimeContext, Integer hour);
}
