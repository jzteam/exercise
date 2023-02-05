package cn.jzteam.work;

import java.util.Date;

public class PhpBankParamBO {
	
	/**
	 * a. Transaction Date
		b. Application Number
		c. Remitter Last Name
		d. Beneficiary Last Name
		e. Settlement Mode
		f. FundingAmount(ifSettlementAmountisnotavailable)
		g. FundingCurrency(if FundingAmount ispresent)
		h. SettlementAmount(ifFundingAmountisnotavailable)
		i. Settlement Currency (if Settlement Amount ispresent)
		j. Bank Code (this should have the fixed value of ‘CBC’)
		k. Bank Name (this should have the fixed value of ‘CHINA BANKING CORPORATION’)
		l. Account Number
	 */
	
	// 交易时间：MMddyyyy
	private Date transactionDate;
	// 应用编号
	private String applicationNumber;
	// 汇款人名字
	private String remitterLastName;
	// 收款人名字
	private String beneficiaryLastName;
	// 汇款金额
	private Double fundingAmount;
	// 汇款货币
	private String fundingCurrency;
	// 收款金额：15位字符串，如000000001825025
	private Double settlementAmount;
	// 收款货币
	private String settlementCurrency;
	
	// 收款类型
	private String settlementMode;
	
	/** 
	 * 根据settlementMode不同，部分必填字段也不同
	 * ‘01’ - Credit to Account
		‘02’ - Credit to Other Bank 
		‘03’ - CBC Branch Pickup 
		‘04’ - Door-To-Door Cash 
		‘05’ - Door-To-Door Product 
		‘06’ - Account Opening
		‘07’ - BillsPayment
		‘08’ - Outlet Pickup
		‘09’ - Pick-up Anywhere 
		‘10’ - Open Card
		‘11’ - Cash Card
	 */
	
	
	// 01 - Credit to Account 
	// 02 - Credit to OtherBank
	// 银行编号
	private String bankCode;
	// 银行名称
	private String bankName;
	// 银行账号
	private String accountNumber;
	
	
	// 03 - CBC Branch Pickup
	
	
	// 08 - Outlet Pickup:ML、CEBUANA
	private String outletCode;
	
	
	// 11 - CashCard
	private String goldCardNumber;
	
	
	// 保存hash，用于累加
	private String hash;


	public Date getTransactionDate() {
		return transactionDate;
	}


	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}


	public String getApplicationNumber() {
		return applicationNumber;
	}


	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}


	public String getRemitterLastName() {
		return remitterLastName;
	}


	public void setRemitterLastName(String remitterLastName) {
		this.remitterLastName = remitterLastName;
	}


	public String getBeneficiaryLastName() {
		return beneficiaryLastName;
	}


	public void setBeneficiaryLastName(String beneficiaryLastName) {
		this.beneficiaryLastName = beneficiaryLastName;
	}


	public String getFundingCurrency() {
		return fundingCurrency;
	}


	public void setFundingCurrency(String fundingCurrency) {
		this.fundingCurrency = fundingCurrency;
	}


	public String getSettlementCurrency() {
		return settlementCurrency;
	}


	public void setSettlementCurrency(String settlementCurrency) {
		this.settlementCurrency = settlementCurrency;
	}


	public String getSettlementMode() {
		return settlementMode;
	}


	public void setSettlementMode(String settlementMode) {
		this.settlementMode = settlementMode;
	}


	public String getBankCode() {
		return bankCode;
	}


	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}


	public String getBankName() {
		return bankName;
	}


	public void setBankName(String bankName) {
		this.bankName = bankName;
	}


	public String getAccountNumber() {
		return accountNumber;
	}


	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}


	public String getOutletCode() {
		return outletCode;
	}


	public void setOutletCode(String outletCode) {
		this.outletCode = outletCode;
	}


	public String getGoldCardNumber() {
		return goldCardNumber;
	}


	public void setGoldCardNumber(String goldCardNumber) {
		this.goldCardNumber = goldCardNumber;
	}


	public Double getFundingAmount() {
		return fundingAmount;
	}


	public void setFundingAmount(Double fundingAmount) {
		this.fundingAmount = fundingAmount;
	}


	public Double getSettlementAmount() {
		return settlementAmount;
	}


	public void setSettlementAmount(Double settlementAmount) {
		this.settlementAmount = settlementAmount;
	}


	public String getHash() {
		return hash;
	}


	public void setHash(String hash) {
		this.hash = hash;
	}
	
}
