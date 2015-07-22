package com.wk.cms.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * 模板附件
 * 
 * @author Administrator
 * 
 */
@Entity
public class TempFile {

	@Id
	private String id;
	@OneToOne(cascade = { CascadeType.ALL })
	private File file;
	
	@ManyToOne
	private Site site;

	@ManyToMany
	private Set<Template> templates;

	public TempFile() {
	}

	public TempFile(String id, Set<Template> template2, File tf, Site site2) {
		this.id = id;
		this.templates = template2;
		this.file = tf;
		this.site = site2;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	
	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Set<Template> getTemplates() {
		return templates;
	}

	public void setTemplates(Set<Template> templates) {
		this.templates = templates;
	}

	@Override
	public boolean equals(Object obj) {

		if (this.site != null && this.file != null
				&& obj instanceof TempFile) {
			TempFile tar = (TempFile) obj;

			return site.getId().equals(tar.getSite().getId())
					&& file.getFileName().equals(tar.getFile().getFileName());
		}
		return super.equals(obj);
	}
}
