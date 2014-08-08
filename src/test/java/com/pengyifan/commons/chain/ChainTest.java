package com.pengyifan.commons.chain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ChainTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();
  @Mock
  private Handler mockHandler1;
  @Mock
  private Handler mockHandler2;
  @Mock
  private Handler mockHandler3;
  @Mock
  private Context context;
  
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testAddHandler_oneHandler() {
    Chain chain = new Chain();
    chain.addHandler(mockHandler1);
    assertEquals(mockHandler1, chain.headHandler);
    assertEquals(mockHandler1, chain.tailHandler);
  }

  @Test
  public void testAddHandler_twoHandlers() {
    Chain chain = new Chain();
    chain.addHandler(mockHandler1)
        .addHandler(mockHandler2);
    assertEquals(mockHandler1, chain.headHandler);
    assertEquals(mockHandler2, chain.tailHandler);
    verify(mockHandler1).setSuccessor(mockHandler2);
    verify(mockHandler2, never()).setSuccessor(any(Handler.class));
  }

  @Test
  public void testAddHandler_threeHandlers() {
    Chain chain = new Chain();
    chain.addHandler(mockHandler1)
        .addHandler(mockHandler2)
        .addHandler(mockHandler3);
    assertEquals(mockHandler1, chain.headHandler);
    assertEquals(mockHandler3, chain.tailHandler);
    verify(mockHandler1).setSuccessor(mockHandler2);
    verify(mockHandler2).setSuccessor(mockHandler3);
    verify(mockHandler3, never()).setSuccessor(any(Handler.class));
  }

  @Test
  public void testExecute_noHandler()
      throws Exception {
    Chain chain = new Chain();
    thrown.expect(NullPointerException.class);
    chain.execute(context);
    assertNull(chain.headHandler);
    assertNull(chain.tailHandler);
  }

  @Test
  public void testHandleRequest_success()
      throws Exception {
    Chain chain = new Chain();
    chain.addHandler(mockHandler1);
    chain.execute(context);
    verify(mockHandler1).execute(context);
  }
}
