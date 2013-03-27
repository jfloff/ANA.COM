package pt.ist.anacom.service.mock;

import static org.easymock.EasyMock.createMock;

import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.xml.registry.infomodel.Service;

import junit.framework.TestCase;

import org.easymock.EasyMock;

import pt.ist.anacom.replication.commands.GetPhoneBalanceStrategy;
import pt.ist.anacom.replication.quorumresponse.QuorumResponseInterface;
import pt.ist.anacom.shared.dto.PhoneSimpleDto;
import pt.ist.anacom.shared.stubs.client.BalanceType;

public class GetBalanceServiceDistTest extends TestCase {

    private final String REPICA_URL_1 = "URL_1";
    private final String REPICA_URL_2 = "URL_2";
    private final String REPICA_URL_3 = "URL_3";
    private final String REPICA_URL_4 = "URL_4";
    
    private final int GOOD_RESPONSE_BALANCE = 1337;
    private final int BYZANTINE_RESPONSE_BALANCE = 666;
    private final int DELAYED_RESPONSE_BALANCE = 101;
    
    private final int VERSION_ONE = 1;
    private final int VERSION_TWO = 2;
    private final int VERSION_THREE = 3;
    
    private BalanceType BALANCE_TYPE_TO_BE_RETURNED;
    
    // class under test
    private GetPhoneBalanceStrategy testClass;

    // mock object
    private QuorumResponseInterface mock;
    
    public GetBalanceServiceDistTest() {
        super();
    }

    @Override
    public void setUp() {
        
        try {
            this.testClass = new GetPhoneBalanceStrategy(null, new PhoneSimpleDto("911111111"), "91"){
                   
                @Override
                protected ArrayList<Service> getReplicas(String prefix){
                    return null;
                }
                @Override
                protected int getReplicaNextVersion(ArrayList<String> urlList){
                    return 0;
                }
                @Override
                protected ArrayList<String> getReplicaURL(String prefix){
                    return null;
                }
                @Override
                protected BalanceType getResponseFromLastReplica() {
                    return BALANCE_TYPE_TO_BE_RETURNED;
                }
                
            };
        } catch (MalformedURLException e) {
        }
        this.mock = createMock(QuorumResponseInterface.class);
    }

    public void testGetBalanceWithoutByzantinoOrDelayed() { // | 1337 1 (4) |

        BalanceType balanceType = getNewBalanceType(GOOD_RESPONSE_BALANCE, VERSION_ONE);
        
        EasyMock.expect(this.mock.getCommand()).andReturn(testClass);
        EasyMock.replay(this.mock);
        
        GetPhoneBalanceStrategy strategy = (GetPhoneBalanceStrategy) mock.getCommand();
        
        strategy.addResponse(REPICA_URL_1, balanceType);
        strategy.addResponse(REPICA_URL_2, balanceType);
        strategy.addResponse(REPICA_URL_4, balanceType);

        testClass.waitQuorum();
        testClass.checkExceptions();
        testClass.vote();
        testClass.getResponse();
        
        int actualBalance = testClass.getBalance().getBalance();
        assertEquals(GOOD_RESPONSE_BALANCE, actualBalance);
    }

    public void testGetBalanceWithOneByzantineDiferentVersion() {//| 1337 1 (3) | 666  2 (1) |

        BalanceType balanceType = getNewBalanceType(GOOD_RESPONSE_BALANCE, VERSION_ONE);
        
        BalanceType byzantineBalanceType = getNewBalanceType(BYZANTINE_RESPONSE_BALANCE, VERSION_TWO);
        
        EasyMock.expect(this.mock.getCommand()).andReturn(testClass);
        EasyMock.replay(this.mock);
        
        GetPhoneBalanceStrategy strategy = (GetPhoneBalanceStrategy) mock.getCommand();
        
        strategy.addResponse(REPICA_URL_1, balanceType);
        strategy.addResponse(REPICA_URL_2, balanceType);
        strategy.addResponse(REPICA_URL_4, byzantineBalanceType);
        strategy.addResponse(REPICA_URL_3, balanceType);

        testClass.waitQuorum();
        testClass.increaseQuorumCounter();
        testClass.increaseQuorumCounter();
        testClass.increaseQuorumCounter();
        testClass.increaseQuorumCounter();
        testClass.checkExceptions();
        testClass.vote();
        testClass.getResponse();
        
        int actualBalance = testClass.getBalance().getBalance();
        assertEquals(GOOD_RESPONSE_BALANCE, actualBalance);
    }
    
    
    public void testGetBalanceWithDelayedAndByzantine() {//| 1337 2 (2) | 666  2 (1) | 101 1 (1) |

        BalanceType balanceType = getNewBalanceType(GOOD_RESPONSE_BALANCE, VERSION_ONE);
        
        BalanceType byzantineBalanceType = getNewBalanceType(BYZANTINE_RESPONSE_BALANCE, VERSION_TWO);
        
        BalanceType delayedBalanceType = getNewBalanceType(DELAYED_RESPONSE_BALANCE, VERSION_THREE);
        
        EasyMock.expect(this.mock.getCommand()).andReturn(testClass);
        EasyMock.replay(this.mock);
        
        GetPhoneBalanceStrategy strategy = (GetPhoneBalanceStrategy) mock.getCommand();
        
        strategy.addResponse(REPICA_URL_1, balanceType);
        strategy.addResponse(REPICA_URL_2, delayedBalanceType);
        strategy.addResponse(REPICA_URL_3, byzantineBalanceType);
        strategy.addResponse(REPICA_URL_4, balanceType);
        
        testClass.waitQuorum();
        testClass.increaseQuorumCounter();
        testClass.increaseQuorumCounter();
        testClass.increaseQuorumCounter();
        testClass.increaseQuorumCounter();
        testClass.checkExceptions();
        testClass.vote();
        testClass.getResponse();
        
        int actualBalance = testClass.getBalance().getBalance();
        assertEquals(GOOD_RESPONSE_BALANCE, actualBalance);
    }
    
    
    public void testGetBalanceWithDelayedAndByzantineExtraQuorum() {//| 1337 2 (1) | 666  3 (1) | 101 1 (1) |

        BalanceType balanceType = getNewBalanceType(GOOD_RESPONSE_BALANCE, VERSION_TWO);
        
        BalanceType byzantineBalanceType = getNewBalanceType(BYZANTINE_RESPONSE_BALANCE, VERSION_THREE);
        
        BalanceType delayedBalanceType = getNewBalanceType(DELAYED_RESPONSE_BALANCE, VERSION_ONE);
        
        EasyMock.expect(this.mock.getCommand()).andReturn(testClass);
        EasyMock.replay(this.mock);

        GetPhoneBalanceStrategy strategy = (GetPhoneBalanceStrategy) mock.getCommand();
        
        strategy.addResponse(REPICA_URL_1, balanceType);
        strategy.addResponse(REPICA_URL_2, delayedBalanceType);
        strategy.addResponse(REPICA_URL_4, byzantineBalanceType);
        BALANCE_TYPE_TO_BE_RETURNED = balanceType;
        
        testClass.waitQuorum();
        testClass.increaseQuorumCounter();
        testClass.increaseQuorumCounter();
        testClass.increaseQuorumCounter();
        testClass.checkExceptions();
        testClass.vote();
        testClass.increaseQuorumCounter();
        testClass.getResponse();
       
        int actualBalance = testClass.getBalance().getBalance();
        assertEquals(GOOD_RESPONSE_BALANCE, actualBalance);
    }

    public void testGetBalanceWithDalayedEqualsToByzantine() {//| 1337 2 (2) | 101  1 (2) |

        BalanceType balanceType = getNewBalanceType(GOOD_RESPONSE_BALANCE, VERSION_TWO);
        
        BalanceType delayedBalanceType = getNewBalanceType(DELAYED_RESPONSE_BALANCE, VERSION_ONE);
        
        BalanceType byzantineBalanceType = delayedBalanceType;
        
        //Excects sobre interface
        EasyMock.expect(this.mock.getCommand()).andReturn(testClass);
        
        //finaliza os expects
        EasyMock.replay(this.mock);

        //chamas a classe de test
        
        GetPhoneBalanceStrategy strategy = (GetPhoneBalanceStrategy) mock.getCommand();
        
        strategy.addResponse(REPICA_URL_1, balanceType);
        strategy.addResponse(REPICA_URL_2, delayedBalanceType);
        strategy.addResponse(REPICA_URL_4, byzantineBalanceType);
        strategy.addResponse(REPICA_URL_3, balanceType);
        
        testClass.waitQuorum();
        testClass.increaseQuorumCounter();
        testClass.increaseQuorumCounter();
        testClass.increaseQuorumCounter();
        testClass.increaseQuorumCounter();
        testClass.checkExceptions();
        testClass.vote();
        
        testClass.getResponse();
       
        int actualBalance = testClass.getBalance().getBalance();
        assertEquals(GOOD_RESPONSE_BALANCE, actualBalance);
    }

    public void testGetBalanceWithDalayedEqualsToByzantineExtraQuorum() {//| 1337 2 (1) | 101  1 (2) |

        BalanceType balanceType = getNewBalanceType(GOOD_RESPONSE_BALANCE, VERSION_TWO);
        
        BalanceType delayedBalanceType = getNewBalanceType(DELAYED_RESPONSE_BALANCE, VERSION_ONE);
        
        BalanceType byzantineBalanceType = delayedBalanceType;
        
        EasyMock.expect(this.mock.getCommand()).andReturn(testClass);
        EasyMock.replay(this.mock);

        GetPhoneBalanceStrategy strategy = (GetPhoneBalanceStrategy) mock.getCommand();
        
        strategy.addResponse(REPICA_URL_1, balanceType);
        strategy.addResponse(REPICA_URL_2, delayedBalanceType);
        strategy.addResponse(REPICA_URL_3, byzantineBalanceType);
        BALANCE_TYPE_TO_BE_RETURNED = balanceType;
        
        
        testClass.waitQuorum();
        testClass.increaseQuorumCounter();
        testClass.increaseQuorumCounter();
        testClass.increaseQuorumCounter();
        testClass.checkExceptions();
        testClass.vote();
        testClass.increaseQuorumCounter();
        testClass.getResponse();
       
        int actualBalance = testClass.getBalance().getBalance();
        assertEquals(GOOD_RESPONSE_BALANCE, actualBalance);
    }

  public void testGetBalanceWithOneByzantineSameVersion() {// | 1337 1 (3) | 666  1 (1) |

      BalanceType balanceType = getNewBalanceType(GOOD_RESPONSE_BALANCE, VERSION_ONE);
      
      BalanceType byzantineBalanceType = getNewBalanceType(BYZANTINE_RESPONSE_BALANCE, VERSION_ONE);
      
      EasyMock.expect(this.mock.getCommand()).andReturn(testClass);
      EasyMock.replay(this.mock);
      
      GetPhoneBalanceStrategy strategy = (GetPhoneBalanceStrategy) mock.getCommand();
      
      strategy.addResponse(REPICA_URL_1, balanceType);
      strategy.addResponse(REPICA_URL_2, balanceType);
      strategy.addResponse(REPICA_URL_4, byzantineBalanceType);
      strategy.addResponse(REPICA_URL_3, balanceType);

      testClass.waitQuorum();
      testClass.checkExceptions();
      testClass.vote();
      testClass.getResponse();
      
      int actualBalance = testClass.getBalance().getBalance();
      assertEquals(GOOD_RESPONSE_BALANCE, actualBalance);
  }
    
    /** Creates a BalanceType with the given arguments.
     * 
     * @param balance
     *          of BalanceType
     * @param version
     *          of BalanceType
     * @return the created BalanceType
     */
    private BalanceType getNewBalanceType(int balance, int version) {
        BalanceType balanceType = new BalanceType();
        balanceType.setBalance(balance);
        balanceType.setVersion(version);
        return balanceType;
    }
 

}

/*
 * Currency testObject = new Currency(2.50, "USD"); Currency expected = new
 * Currency(3.75,"EUR"); ExchangeRate mock = EasyMock.createMock(ExchangeRate.class);
 * EasyMock.expect(mock.getRate("USD", "EUR")).andReturn(1.5); EasyMock.replay(mock);
 * Currency actual = testObject.toEuros(mock); assertEquals(expected, actual); }
 */

