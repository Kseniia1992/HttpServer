package org.httpserver.logic;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * Класс для описания сущности Url
 * @author Kseniia
 *
 */
@NamedQueries({
	@NamedQuery(name="Url.getUrlCount", query="SELECT COUNT (u) FROM Url u"),	
})

@Entity
@Table(name="url")
public class Url implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int urlid; 
    private String url;
    private Timestamp data;
    private Set<Ip>ip_list;
	
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) 
    @Column(name="urlid")
    public int getUrlid() {
		return urlid;
	}
    
	public void setUrlid(int urlid) {
		this.urlid = urlid;
	}
	
	@Column(name="url")
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Column(name="data")
	public Timestamp getData() {
		return data;
	}
	
	public void setData(Timestamp data) {
		this.data = data;
	}
	
	//объединение таблиц (many-to-many)
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "ip_url",
	joinColumns = {
	@JoinColumn(name="urlId") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="ipId")
	}
	)
	public Set<Ip> getIp_list() {
		return ip_list;
	}
	
	public void setIp_list(Set<Ip> ip_list) {
		this.ip_list = ip_list;
	}   
    
}
