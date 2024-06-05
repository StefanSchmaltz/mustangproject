package org.mustangproject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.mustangproject.ZUGFeRD.IReferencedDocument;
import org.mustangproject.ZUGFeRD.IZUGFeRDAllowanceCharge;
import org.mustangproject.ZUGFeRD.IZUGFeRDExportableItem;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/***
 * describes any invoice line
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item implements IZUGFeRDExportableItem {
	protected BigDecimal price, quantity, tax, grossPrice, lineTotalAmount;
	protected BigDecimal basisQuantity = BigDecimal.ONE;
	protected Date detailedDeliveryPeriodFrom = null, detailedDeliveryPeriodTo = null;
	protected String id, unit, unitBasis;
	protected String referencedLineID = null;
	protected Product product;
	protected ArrayList<String> notes = null;
	protected ArrayList<ReferencedDocument> referencedDocuments = null;
	protected ArrayList<IZUGFeRDAllowanceCharge> Allowances = new ArrayList<IZUGFeRDAllowanceCharge>(),
			Charges = new ArrayList<IZUGFeRDAllowanceCharge>();

	/***
	 * default constructor
	 * 
	 * @param product  contains the products name, tax rate, and unit
	 * @param price    the base price of one item the product
	 * @param quantity the number, dimensions or the weight of the delivered product
	 *                 or good in this context
	 */
	public Item(Product product, BigDecimal price, BigDecimal quantity) {
		this.price = price;
		this.quantity = quantity;
		this.product = product;
	}

	/***
	 * empty constructor do not use, but might be used e.g. by jackson
	 */
	public Item() {
	}

	public Item addReferencedLineID(String s) {
		referencedLineID = s;
		return this;
	}

	/***
	 * BT 132 (issue https://github.com/ZUGFeRD/mustangproject/issues/247)
	 * 
	 * @return the line ID of the order (BT132)
	 */
	@Override
	public String getBuyerOrderReferencedDocumentLineID() {
		return referencedLineID;
	}

	public BigDecimal getLineTotalAmount() {
		return lineTotalAmount;
	}

	/**
	 * should only be set by calculator classes or maybe when reading from XML
	 * 
	 * @param lineTotalAmount price*quantity of this line
	 * @return fluent setter
	 */
	public Item setLineTotalAmount(BigDecimal lineTotalAmount) {
		this.lineTotalAmount = lineTotalAmount;
		return this;
	}

	public BigDecimal getGrossPrice() {
		return grossPrice;
	}

	/***
	 * the list price without VAT (sic!), refer to EN16931-1 for definition
	 * 
	 * @param grossPrice the list price without VAT
	 * @return fluent setter
	 */
	public Item setGrossPrice(BigDecimal grossPrice) {
		this.grossPrice = grossPrice;
		return this;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public Item setTax(BigDecimal tax) {
		this.tax = tax;
		return this;
	}

	public Item setId(String id) {
		this.id = id;
		return this;
	}

	public String getId() {
		return id;
	}

	@Override
	public BigDecimal getPrice() {
		return price;
	}

	public Item setPrice(BigDecimal price) {
		this.price = price;
		return this;
	}

	@Override
	public BigDecimal getQuantity() {
		return quantity;
	}

	public Item setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
		return this;
	}

	@Override
	public BigDecimal getBasisQuantity() {
		return basisQuantity;
	}

	public Item setBasisQuantity(BigDecimal basisQuantity) {
		this.basisQuantity = basisQuantity;
		return this;
	}

	@Override
	public Product getProduct() {
		return product;
	}

	@Override
	public IZUGFeRDAllowanceCharge[] getItemAllowances() {
		if (Allowances.isEmpty()) {
			return null;
		} else
			return Allowances.toArray(new IZUGFeRDAllowanceCharge[0]);
	}

	@Override
	public IZUGFeRDAllowanceCharge[] getItemCharges() {
		if (Charges.isEmpty()) {
			return null;
		} else
			return Charges.toArray(new IZUGFeRDAllowanceCharge[0]);
	}

	@Override
	public String[] getNotes() {
		if (notes == null) {
			return null;
		}
		return notes.toArray(new String[0]);
	}

	public Item setProduct(Product product) {
		this.product = product;
		return this;
	}

	/***
	 * Adds a item level addition to the price (will be multiplied by quantity)
	 * 
	 * @see org.mustangproject.Charge
	 * @param izac a relative or absolute charge
	 * @return fluent setter
	 */
	public Item addCharge(IZUGFeRDAllowanceCharge izac) {
		Charges.add(izac);
		return this;
	}

	/***
	 * Adds a item level reduction the price (will be multiplied by quantity)
	 * 
	 * @see org.mustangproject.Allowance
	 * @param izac a relative or absolute allowance
	 * @return fluent setter
	 */
	public Item addAllowance(IZUGFeRDAllowanceCharge izac) {
		Allowances.add(izac);
		return this;
	}

	/***
	 * adds item level freetext fields (includednote)
	 * 
	 * @param text UTF8 plain text
	 * @return fluent setter
	 */
	public Item addNote(String text) {
		if (notes == null) {
			notes = new ArrayList<String>();
		}
		notes.add(text);
		return this;
	}

	/***
	 * adds item level Referenced documents along with their typecodes and
	 * issuerassignedIDs
	 * 
	 * @param doc the ReferencedDocument to add
	 * @return fluent setter
	 */
	public Item addReferencedDocument(ReferencedDocument doc) {
		if (referencedDocuments == null) {
			referencedDocuments = new ArrayList<ReferencedDocument>();
		}
		referencedDocuments.add(doc);
		return this;
	}

	@Override
	public IReferencedDocument[] getReferencedDocuments() {
		if (referencedDocuments == null) {
			return null;
		}
		return referencedDocuments.toArray(new IReferencedDocument[0]);
	}

	/***
	 * specify a item level delivery period (apart from the document level delivery
	 * period, and the document level delivery day, which is probably anyway
	 * required)
	 *
	 * @param from start date
	 * @param to   end date
	 * @return fluent setter
	 */
	public Item setDetailedDeliveryPeriod(Date from, Date to) {
		detailedDeliveryPeriodFrom = from;
		detailedDeliveryPeriodTo = to;
		return this;
	}

	/***
	 * specifies the item level delivery period (there is also one on document
	 * level), this will be included in a BillingSpecifiedPeriod element
	 * 
	 * @return the beginning of the delivery period
	 */
	public Date getDetailedDeliveryPeriodFrom() {
		return detailedDeliveryPeriodFrom;
	}

	/***
	 * specifies the item level delivery period (there is also one on document
	 * level), this will be included in a BillingSpecifiedPeriod element
	 * 
	 * @return the end of the delivery period
	 */
	public Date getDetailedDeliveryPeriodTo() {
		return detailedDeliveryPeriodTo;
	}

	@Override
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public String getUnitBasis() {
		return unitBasis;
	}

	public void setUnitBasis(String unitBase) {
		this.unitBasis = unitBase;
	}

}
