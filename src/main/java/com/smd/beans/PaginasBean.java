package com.smd.beans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.UploadedFile;

import com.smd.model.Pagina;

@ManagedBean
@SessionScoped
public class PaginasBean {

	private List<Pagina> paginas;
	private final String destination = "/Users/danielcarlos/Documents/Development/workspace/JavaEE/Web2Mobile/src/main/webapp/";
	private UploadedFile file;
	private UploadedFile audio;
	private String texto;

	public PaginasBean() {

	}

	public String submit() {
		try {
			Pagina p = new Pagina();
			p.setTexto(texto);
			if (file != null) {
				copyFile("/images/"+file.getFileName(), file.getInputstream());
				FacesMessage msg = new FacesMessage("Sucesso", file.getFileName()
						+ " foi adicionado.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				p.setUrlImagem(file.getFileName());
			}
			if (audio != null) {
				copyFile("/media/"+audio.getFileName(), audio.getInputstream());
				FacesMessage msg = new FacesMessage("Sucesso", audio.getFileName()
						+ " foi adicionado.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				
				p.setUrlSom(audio.getFileName());
			}
			
			if (paginas == null) {
				System.out.println("paginas null");
				paginas = new ArrayList<Pagina>();
			}
			paginas.add(p);
			return "sucesso";
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
	}

	public void copyFile(String fileName, InputStream in) {
		try {

			OutputStream out = new FileOutputStream(new File(destination + "/"
					+ fileName));

			int read = 0;

			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1) {

				out.write(bytes, 0, read);

			}
			in.close();

			out.flush();

			out.close();

			System.out.println("New file created!");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Pagina> getPaginas() {

		return paginas;

	}

	public void setPaginas(List<Pagina> paginas) {
		this.paginas = paginas;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public UploadedFile getAudio() {
		return audio;
	}

	public void setAudio(UploadedFile audio) {
		this.audio = audio;
	}


	
	
}
