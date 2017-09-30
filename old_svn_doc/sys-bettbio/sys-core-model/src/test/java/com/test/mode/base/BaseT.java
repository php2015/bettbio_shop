package com.test.mode.base;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(locations="classpath:spring-test-config.xml")
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=false)
public class BaseT extends AbstractTransactionalJUnit4SpringContextTests{

}
