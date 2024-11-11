package pe.mm.reception.core.dto;

import net.sf.oval.constraint.NotNull;

import java.util.Date;

public class BatchDTO {

	@NotNull(profiles = { "update" })
	private Integer id;
	@NotNull(profiles = { "insert", "update" })
	private Integer lineId;
	@NotNull(profiles = { "insert", "update" })
	private Date started;
	private Date ended;
	@NotNull(profiles = { "insert", "update" })
	private Integer productId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLineId() {
		return lineId;
	}

	public void setLineId(Integer lineId) {
		this.lineId = lineId;
	}

	public Date getStarted() {
		return started;
	}

	public void setStarted(Date started) {
		this.started = started;
	}

	public Date getEnded() {
		return ended;
	}

	public void setEnded(Date ended) {
		this.ended = ended;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}
}