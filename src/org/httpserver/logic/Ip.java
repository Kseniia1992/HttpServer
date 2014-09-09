package org.httpserver.logic;

import java.io.Serializable;
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
import javax.persistence.Table;

/**
 * Класс для описания сущности Ip
 * @author Kseniia
 *
 */
@Entity
@Table(name="ip")
public class Ip  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int ipid;
    private String ip;
    private Set<Url> url_list;
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)    
    @Column(name="ipid")
	public int getIpid() {
		return ipid;
	}
    
	public void setIpid(int ipid) {
		this.ipid = ipid;
	}
	
	@Column(name="ip")
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	//объединение таблиц (many-to-many)
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "ip_url",
	joinColumns = {
	@JoinColumn(name="ipId") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="urlId")
	}
	)
	public Set<Url> getUrl_list() {
		return url_list;
	}
	
	public void setUrl_list(Set<Url> url_list) {
		this.url_list = url_list;
	}	
	
}
