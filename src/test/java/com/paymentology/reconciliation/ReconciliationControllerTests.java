package com.paymentology.reconciliation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Description;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;

import com.paymentology.reconciliation.entities.Reconciliation;
import com.paymentology.reconciliation.entities.Transaction;
import com.paymentology.reconciliation.entities.TransactionReconciliation;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ReconciliationControllerTests {
	private static final String rootPath = "/reconciliations";
	private static final String comparesEndpoint = rootPath + "/compares";
	private static final String correlationsEndpoint = rootPath + "/correlations";

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	@Description("Calls the endpoint reconcoliation/compares and return the a json with the structure of TransactionsComparison.")
	void testComparesEndpoint() throws Exception {
		TransactionReconciliation tExpected = getExpectedReconciliation();

		// Create the endpoint input parameters
		LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
		parameters.add("afile", getFileA());
		parameters.add("bfile", getFileB());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
				parameters, headers);

		// Call endpoint /reaconcilations/compares
		ResponseEntity<TransactionReconciliation> response = this.restTemplate.exchange(
				"http://localhost:" + port + comparesEndpoint,
				HttpMethod.POST,
				entity,
				TransactionReconciliation.class);

		// Check the response
		assertThat(response.getBody())
				.usingRecursiveComparison()
				.isEqualTo(tExpected);

	}

	@Test
	@Description("Calls the endpoint reconcoliation/correlations and return the a json with the structure of TransactionsComparison.")
	void testCorrelationsEndpoint() throws Exception {

		// Create the endpoint input parameters
		LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
		parameters.add("file", getCorrelationsFile());
		parameters.add("transaction", getTransactionA());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
				parameters, headers);

		// Call endpoint /reaconcilations/compares
		ResponseEntity<List<Transaction>> response = this.restTemplate.exchange(
				"http://localhost:" + port + correlationsEndpoint,
				HttpMethod.POST,
				entity,
				new ParameterizedTypeReference<List<Transaction>>() {
            });

		// Check the response
		assertThat(response.getBody())
				.usingRecursiveComparison()
				.isEqualTo(expectedTransactionList());

	}

	public Transaction getTransactionA() {
        Transaction ta = new Transaction();
        ta.setProfileName("Card Campaign");
        ta.setTransactionDate(LocalDateTime.ofInstant(Instant.parse("2014-01-11T22:27:44.00Z"), ZoneOffset.UTC));
        ta.setTransactionAmount(-20000l);
        ta.setTransactionNarrative("*MOLEPS ATM25             MOLEPOLOLE    BW");
        ta.setTransactionDescription("DEDUCT");
        ta.setTransactionID(584011808649511l);
        ta.setTransactionType(1);
        ta.setWalletReference("P_NzI2ODY2ODlfMTM4MjcwMTU2NS45MzA5");

        return ta;
    }

	private TransactionReconciliation getExpectedReconciliation() {
		TransactionReconciliation expectedReconciliation = new TransactionReconciliation();
		expectedReconciliation.setComparationA(getExpectedReconciliationA());
		expectedReconciliation.setComparationB(getExpectedReconciliationB());

		return expectedReconciliation;
	}

	private ClassPathResource getFileA() {
		return new org.springframework.core.io.ClassPathResource("./transactionsA.csv");
	}

	private ClassPathResource getFileB() {
		return new org.springframework.core.io.ClassPathResource("./transactionsB.csv");
	}

	private ClassPathResource getCorrelationsFile() {
		return new org.springframework.core.io.ClassPathResource("./transactionsCorrelation.csv");
	}

	private Reconciliation getExpectedReconciliationA() {
		Reconciliation reconciliation = new Reconciliation();
		reconciliation.setFileName("transactionsA.csv");
		reconciliation.setTotalRecords(305l);
		reconciliation.setMatchedRecords(288l);
		reconciliation.setUnmatchedRecords(17l);
		reconciliation.setUnmatchedList(Arrays.asList(
				Transaction.newInstance("Card Campaign,2014-01-12 05:33:22,-10000,ENGEN TSOLAMOSESI         GABORONE      BW,DEDUCT,0384012056029314,1,P_NzUyMDI4NjRfMTM4NTM2NjE4OC44Njcy,"),
				Transaction.newInstance("Card Campaign,2014-01-12 05:33:22,-10000,ENGEN TSOLAMOSESI         GABORONE      BW,DEDUCT,0384012056029314,1,P_NzUyMDI4NjRfMTM4NTM2NjE4OC44Njcy,"),
				Transaction.newInstance("Card Campaign,2014-01-12 08:13:22,-50000,*NORTHGATE MALL           GABORONE      BW,DEDUCT,0584012296020384,1,P_NzY1NTAxNDZfMTM4ODM5MjU4My4xMTQ3,"),
				Transaction.newInstance("Card Campaign,2014-01-12 08:22:58,-6220,Choppies Superst102631    Lobatse       BW,DEDUCT,0284012303585533,0,P_NzI5NzYzMjlfMTM3ODMwMDI2Ny40MzUy,"),
				Transaction.newInstance("Card Campaign,2014-01-12 10:09:46,-5395,Choppies Hyper R100256    Lobatse       BW,DEDUCT,0164012367663009,0,P_Nzc0NzIzNjlfMTM4MzkwMjg0Ny41NTIy,"),
				Transaction.newInstance("Card Campaign,2014-01-12 10:39:59,-11490,169473 CHOPPIES SUPER STO BOTSWANA      BW,DEDUCT,0164012239997884,0,P_Nzc0ODIwMzZfMTM4MzMyMDk3Mi4wOTMx,"),
				Transaction.newInstance("Card Campaign,2014-01-12 11:11:09,-30000,THAMAGA ATM               BWS           BW,DEDUCT,0384012258699343,1,P_NzQ0OTE5NjVfMTM4NzU0OTg0My44MzM=,"),
				Transaction.newInstance("Card Campaign,2014-01-12 11:15:11,-2205,548817 PAY-LESS (PTY) LTD BOTSWANA      BW,DEDUCT,0164012261115601,0,P_NzIxMzExMDZfMTM4NjMyNzk1MS4wNDE2,"),
				Transaction.newInstance("Card Campaign,2014-01-12 14:16:24,-10000,TRUWORTHS                 BOTSWANA      BW,DEDUCT,0384012369849844,1,P_NzI3NDUxNDRfMTM4NDM1MDQ5NC4yMTUx,"),
				Transaction.newInstance("Card Campaign,2014-01-12 14:47:52,-19770,578939 WIMPY PHAKALANE    BOTSWANA      BW,DEDUCT,0284012388727189,0,P_NzI2NDM5OTlfMTM4MzY1MzIwNS4yMzQ=,"),
				Transaction.newInstance("Card Campaign,2014-01-12 15:03:05,-6984,128552 P G TIMBERS MAUN   BOTSWANA      BW,DEDUCT,0084012397854064,0,P_Nzc1MDA3MTFfMTM4MTQ4MjU0MC4zMTc2,"),
				Transaction.newInstance("Card Campaign,2014-01-12 15:13:43,-12220,162502 CHOPPIES           BOTSWANA      BW,DEDUCT,0164012404238247,0,P_Nzc1MDc0MTNfMTM4NTgwOTEwNS4wMjk0,"),
				Transaction.newInstance("Card Campaign,2014-01-12 15:26:23,-30000,EASY PLAN RAILWAY         GABORONE      BW,DEDUCT,0584012412153040,1,P_NzQzNjAxNTlfMTM4NjIzNjEzMS4wNzQ5,"),
				Transaction.newInstance("Card Campaign,2014-01-12 15:55:13,-50000,MAHALAPYE BRANCH          BOTSWANA      BW,DEDUCT,0478928932843767,1,P_NzQ0NDExNzFfMTM4NzI3NDgxMC42OTc5,"),
				Transaction.newInstance("Card Campaign,2014-01-12 16:09:09,-19403,*MEGAWATT PARK            PALAPYE       BW,DEDUCT,0384012581494519,1,P_NzY1MDgxNDdfMTM4NDc3NTIxNi4wNjk1,"),
				Transaction.newInstance("Card Campaign,2014-01-12 16:18:25,-35000,*PALAPYE ENGEN            PALAPYE       BW,DEDUCT,0384012587059924,1,P_NzI5MTc3NzdfMTM4MDUyOTMyOS4wNjI4,"),
				Transaction.newInstance("Card Campaign,2014-01-12 16:25:37,-20954,ENGEN WIRED              GABARONE       BW,DEDUCT,0528738473949230,1,P_NzUI4398F03ghjffMOIF8340Mi45NTc4,")
		));
		return reconciliation;
	}

	private Reconciliation getExpectedReconciliationB() {
		Reconciliation reconciliation = new Reconciliation();
		reconciliation.setFileName("transactionsB.csv");
		reconciliation.setTotalRecords(306l);
		reconciliation.setMatchedRecords(290l);
		reconciliation.setUnmatchedRecords(16l);
		reconciliation.setUnmatchedList(Arrays.asList(
				Transaction.newInstance("Card Campaign,2014-01-12 05:33:22,-32400,ENGEN TSOLAMOSESI         GABORONE      BW,DEDUCT,0384012056029314,1,P_NzUyMDI4NjRfMTM4NTM2NjE4OC44Njcy,"),
				Transaction.newInstance("Card Campaign,2014-01-12 08:13:22,-50000,*NRTHGT MLL           GABORONE      BW,DEDUCT,0584012296020384,1,P_NzY1NTAxNDZfMTM4ODM5MjU4My4xMTQ3,"),
				Transaction.newInstance("Card Campaign,2014-01-12 08:22:58,-6220,Choppies Superst102631    Lobatse       BW,DEDUCT,0284349965585533,0,P_NzI5NzYzMjlfMTM3ODMwMDI2Ny40MzUy,"),
				Transaction.newInstance("Card Campaign,2014-01-12 14:09:36,-5395,Choppies Hyper R100256    Lobatse       BW,DEDUCT,0164012367663009,0,P_Nzc0NzIzNjlfMTM4MzkwMjg0Ny41NTIy,"),
				Transaction.newInstance("Card Campaign,2014-01-12 10:39:59,-11490,*Edgars SDTN      ZA,DEDUCT,0164012239997884,0,P_Nzc0ODIwMzZfMTM4MzMyMDk3Mi4wOTMx,"),
				Transaction.newInstance("Card Campaign,2014-01-13 11:11:09,-30000,THAMAGA ATM               BWS           BW,DEDUCT,0384012258699343,1,P_NzQ0OTE5NjVfMTM4NzU0OTg0My44MzM=,"),
				Transaction.newInstance("Card Campaign,2014-01-12 14:21:22,-5466,*RED SQUARE SZ,DEDUCT,8948594584958495,0,P_NzIxMzExMDZfMTM4NjMyNzk1MS4wNDE2,"),
				Transaction.newInstance("Card Campaign,2014-01-12 14:16:24,-10000,TRUWORTHS                 BOTSWANA      BW,DEDUCT,4049540590428544,1,P_NzI3NDUxNDRfMTM4NDM1MDQ5NC4yMTUx,"),
				Transaction.newInstance("Card Campaign,2014-01-12 14:47:52,-39405,578939 WIMPY PHAKALANE    BOTSWANA      BW,DEDUCT,0284012388727189,0,P_NzI2NDM5OTlfMTM4MzY1MzIwNS4yMzQ=,"),
				Transaction.newInstance("Card Campaign,2014-01-12 15:03:05,-6984,128552 P G TIMBERS MAUN   BOTSWANA      BW,DEDUCT,0084012397854064,0,,"),
				Transaction.newInstance("Card Campaign,2014-01-12 21:33:51,-12220,162502 CHOPPIES           BOTSWANA      BW,DEDUCT,0164012404238247,0,P_Nzc1MDc0MTNfMTM4NTgwOTEwNS4wMjk0,"),
				Transaction.newInstance("Card Campaign,2014-01-12 15:26:54,-30000,EASY PLAN RAILWAY         GABORONE      BW,DEDUCT,0584012412153040,1,P_NzQzNjAxNTlfMTM4NjIzNjEzMS4wNzQ5,"),
				Transaction.newInstance("Card Campaign,2014-01-12 15:55:13,-50000,MAHALAPYE BRANCH          BOTSWANA      BW,DEDUCT,0384012429135767,1,P_NzQ0NDExNzFfMTM4NzI3NDgxMC42OTc5,"),
				Transaction.newInstance("Card Campaign,2014-01-12 16:09:09,-20000,*MAUN ENGEN               MAUN          BW,DEDUCT,0384012581494519,1,P_NzY1MDgxNDdfMTM4NDc3NTIxNi4wNjk1,"),
				Transaction.newInstance("Card Campaign,2014-01-12 11:55:43,-37582,*JDOREY STNGER            GABORE        BW,DEDUCT,0475845786555522,1,P_NzI5MTc3u89FITTldfi7349yOS4wNjI4,"),
				Transaction.newInstance("Card Campaign,2014-01-12 20:55:42,-100000,DINOS SHOPPERS            BOTSWANA      BW,DEDUCT,0584012609420666,1,P_NzI5NjY5NDlfMTM4MTc1MDM1MS4zODg2,")));

		return reconciliation;
	}

	public Transaction getTransactionB() {
        Transaction tb = new Transaction();
        tb.setProfileName("Card Campaign");
        tb.setTransactionDate(LocalDateTime.ofInstant(Instant.parse("2014-01-11T22:39:11.00Z"), ZoneOffset.UTC));
        tb.setTransactionAmount(-10000l);
        tb.setTransactionNarrative("*MOGODITSHANE2            MOGODITHSANE  BW");
        tb.setTransactionDescription("DEDUCT");
        tb.setTransactionID(584011815513406l);
        tb.setTransactionType(1);
        tb.setWalletReference("P_NzI1MjA1NjZfMTM3ODczODI3Mi4wNzY5");

        return tb;
    }

    public Transaction getTransactionC() {
        Transaction tc = new Transaction();
        tc.setProfileName("Card Campaign");
        tc.setTransactionDate(LocalDateTime.ofInstant(Instant.parse("2014-01-11T23:28:11.00Z"), ZoneOffset.UTC));
        tc.setTransactionAmount(-5000l);
        tc.setTransactionNarrative("CAPITAL BANK              MOGODITSHANE  BW");
        tc.setTransactionDescription("DEDUCT");
        tc.setTransactionID(464011844938429l);
        tc.setTransactionType(1);
        tc.setWalletReference("P_NzI0NjE1NzhfMTM4NzE4ODExOC43NTYy");

        return tc;
    }

    public List<Transaction> expectedTransactionList() {
        Transaction tb = getTransactionB();
        tb.setCorrelation(0.3145896f);

        Transaction tc =getTransactionC();
        tc.setCorrelation(0.5010092f);
        
        List<Transaction> tlist = new ArrayList<>();
        tlist.add(tb);
        tlist.add(tc);

        return tlist;
    }
}
