package com.smd.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.model.UploadedFile;

import com.smd.model.Pagina;
import com.smd.model.ZipCompressor;

@ManagedBean
@SessionScoped
public class PaginasBean {

	private String nomeProjeto;
	private List<Pagina> paginas = new ArrayList<Pagina>();;
	private UploadedFile imagem;
	private UploadedFile audio;
	private String texto;
	private Pagina paginaAtual = new Pagina();

	public PaginasBean() {
	}

	public String finalizarProjeto(){
		System.out.println("entrou no metodo finalizar");
		ServletContext servletContext = (ServletContext) FacesContext
				.getCurrentInstance().getExternalContext().getContext();
		String webRoot = servletContext.getRealPath(File.separator);		
		
		try {
			new File(nomeProjeto + File.separator + "Audios").mkdirs();
			
			ZipCompressor zip = new ZipCompressor();
			zip.createZip(nomeProjeto + File.separator + nomeProjeto + ".zip");
						
			PrintWriter xml = new PrintWriter(nomeProjeto + File.separator + 
					nomeProjeto + ".xml");
			xml.println("<quadrinho nome=\"" + nomeProjeto + "\">");
			int telaNum = 1;
			for (Pagina pagina : paginas) {
				xml.println("<tela id=\"" + telaNum + "\">");
				
				if(pagina.getUrlImagem() != null)
				{
					copyFile(new File(webRoot + File.separator 
							+ "images" + File.separator + pagina.getUrlImagem()), 
							new File(nomeProjeto + File.separator + pagina.getUrlImagem()));
					zip.addFile(nomeProjeto + File.separator + pagina.getUrlImagem());
					xml.println("<imagem src=\"" + pagina.getUrlImagem() + "\"/>");
				}
				
				if(pagina.getUrlSom() != null)
				{
					copyFile(new File(webRoot + File.separator + 
							"media" + File.separator + pagina.getUrlSom()), 
							new File(nomeProjeto + File.separator + 
									"Audios" + File.separator + pagina.getUrlSom()));
					zip.addFile(nomeProjeto + File.separator + "Audios" + 
									File.separator + pagina.getUrlSom());
					xml.println("<audio src=\"" + pagina.getUrlSom() + "\"/>");
				}
				
				xml.println("<texto txt=\"" + pagina.getTexto() + "\"/>");
				
				xml.println("</tela>");
				telaNum++;
			}
			
			xml.print("</quadrinho>");
			xml.close();
			
			zip.addFile(nomeProjeto + File.separator + 
					nomeProjeto + ".xml");
			zip.generateZip();
			
			FacesMessage msg = new FacesMessage("Sucesso", "Projeto exportado");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			new File(webRoot + File.separator + "resources").mkdir();
			copyFile(new File(nomeProjeto + File.separator + nomeProjeto + ".zip"), 
					new File(webRoot + File.separator + "resources" + File.separator + 
					nomeProjeto + ".zip"));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "sucesso";
	}
	
	public String novaPagina() {
		paginaAtual = new Pagina();
		return submit();
	}
	
	public String novoProjeto() {
		return "sucesso";
	}

	public String deletar() {
		Pagina temp = getPaginaAnterior();
		if(temp==paginaAtual){
			temp = getProximaPagina();
			if(temp==paginaAtual){
				temp = null;
			}
		}
		paginas.remove(paginaAtual);
		paginaAtual = temp;
		return "sucesso";
	}

	public int getIndicePaginaAtual() {
		return paginas.indexOf(paginaAtual) + 1;
	}

	public boolean isContemAnterior() {
		Pagina anterior = new Pagina();
		ListIterator<Pagina> listIterator = paginas.listIterator();
		int indexOf = paginas.indexOf(paginaAtual);
		for (int i = 0; i < indexOf + 1; i++) {
			anterior = listIterator.next();
		}
		if (listIterator.hasPrevious()) {
			anterior = listIterator.previous();
		}
		if (listIterator.hasPrevious()) {
			anterior = listIterator.previous();
			System.out.println("anterior" + anterior.getTexto());
			return true;
		} else {
			return false;
		}
	}

	public boolean isContemProxima() {
		ListIterator<Pagina> listIterator = paginas.listIterator();
		int indexOf = paginas.indexOf(paginaAtual);
		Pagina proxima = new Pagina();
		for (int i = 0; i < indexOf + 1; i++) {
			proxima = listIterator.next();
		}
		if (listIterator.hasNext()) {
			proxima = listIterator.next();
			System.out.println("proxima" + proxima.getTexto());
			return true;
		} else {
			return false;
		}
	}

	public Pagina getPaginaAnterior() {
		Pagina anterior = new Pagina();
		ListIterator<Pagina> listIterator = paginas.listIterator();
		int indexOf = paginas.indexOf(paginaAtual);
		for (int i = 0; i < indexOf + 1; i++) {
			anterior = listIterator.next();
		}
		if (listIterator.hasPrevious()) {
			anterior = listIterator.previous();
		}
		if (listIterator.hasPrevious()) {
			anterior = listIterator.previous();
			System.out.println("anterior" + anterior.getTexto());
		} else {
			anterior = paginaAtual;
		}
		return anterior;
	}

	public Pagina getProximaPagina() {
		ListIterator<Pagina> listIterator = paginas.listIterator();
		int indexOf = paginas.indexOf(paginaAtual);
		Pagina proxima = new Pagina();
		for (int i = 0; i < indexOf + 1; i++) {
			proxima = listIterator.next();
		}
		if (listIterator.hasNext()) {
			proxima = listIterator.next();
			System.out.println("proxima" + proxima.getTexto());
		} else {
			proxima = paginaAtual;
		}
		return proxima;
	}

	public String anterior() {
		imagem = null;
		audio = null;
		texto = null;

		paginaAtual = getPaginaAnterior();

		return "sucesso";
	}

	public String proxima() {
		imagem = null;
		audio = null;
		texto = null;

		paginaAtual = getProximaPagina();

		return "sucesso";
	}

	public String submit() {
		try {

			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			String webRoot = servletContext.getRealPath(File.separator);

			System.out.println(webRoot);
			if (texto != null) {
				paginaAtual.setTexto(texto);
			}
			if (imagem != null) {
				System.out.println("imagem nao eh nula");
				copyFile(webRoot + File.separator + "images" + File.separator
						+ imagem.getFileName(), imagem.getInputstream());
				FacesMessage msg = new FacesMessage("Sucesso",
						imagem.getFileName() + " foi adicionado.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				paginaAtual.setUrlImagem(imagem.getFileName());
				System.out.println("urlImagem: " + paginaAtual.getUrlImagem());
			}
			if (audio != null) {
				copyFile(webRoot + File.separator + "media" + File.separator
						+ audio.getFileName(), audio.getInputstream());
				FacesMessage msg = new FacesMessage("Sucesso",
						audio.getFileName() + " foi adicionado.");
				FacesContext.getCurrentInstance().addMessage(null, msg);

				paginaAtual.setUrlSom(audio.getFileName());
				System.out.println("urlSom: " + paginaAtual.getUrlSom());
			}
			if (!paginas.contains(paginaAtual)) {
				System.out.println("nao contem");
				paginas.add(paginaAtual);
			}
			return "sucesso";
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
	}

	public void copyFile(String fileName, InputStream in) {
		try {

			OutputStream out = new FileOutputStream(new File(fileName));

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
	
	public void copyFile(File source, File destination) throws IOException {     
	     if (destination.exists())     
	         destination.delete();     
	    
	     FileChannel sourceChannel = null;     
	     FileChannel destinationChannel = null;     
	    
	     try {     
	         sourceChannel = new FileInputStream(source).getChannel();     
	         destinationChannel = new FileOutputStream(destination).getChannel();     
	         sourceChannel.transferTo(0, sourceChannel.size(),     
	                 destinationChannel);     
	     } finally {     
	         if (sourceChannel != null && sourceChannel.isOpen())     
	             sourceChannel.close();     
	         if (destinationChannel != null && destinationChannel.isOpen())     
	             destinationChannel.close();     
	    }     
	}

	public List<Pagina> getPaginas() {

		return paginas;

	}

	public void setPaginas(List<Pagina> paginas) {
		this.paginas = paginas;
	}

	public UploadedFile getImagem() {
		return imagem;
	}

	public void setImagem(UploadedFile imagem) {
		this.imagem = imagem;
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

	public Pagina getPaginaAtual() {
		return paginaAtual;
	}

	public void setPaginaAtual(Pagina paginaAtual) {
		this.paginaAtual = paginaAtual;
	}

	public String getNomeProjeto() {
		return nomeProjeto;
	}

	public void setNomeProjeto(String nomeProjeto) {
		this.nomeProjeto = nomeProjeto;
	}

}
