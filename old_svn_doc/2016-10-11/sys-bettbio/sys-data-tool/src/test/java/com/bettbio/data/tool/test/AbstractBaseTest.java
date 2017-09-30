package com.bettbio.data.tool.test;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

@ContextConfiguration(locations="classpath:spring-data-tool.xml")
//@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=false)
public class AbstractBaseTest extends AbstractTransactionalJUnit4SpringContextTests{

}
