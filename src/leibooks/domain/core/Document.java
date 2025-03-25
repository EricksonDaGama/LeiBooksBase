package leibooks.domain.core;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.*;
import leibooks.utils.AbsSubject;
import leibooks.utils.RegExpMatchable;

public class Document extends AbsSubject<DocumentEvent> implements IDocument, RegExpMatchable {

	private String title;
	private String author;
	private final String mimeType;
	private final LocalDate modifiedDate;
	private final File file;
	private final Optional<Integer> numPages;
	private int lastPageVisited;
	private final Map<Integer, Page> pages = new TreeMap<>();

	public Document(String title, String author, LocalDate modifiedDate,
					String mimeType, String pathToFile, Optional<Integer> numPages) {
		this.title = title;
		this.author = author;
		this.modifiedDate = modifiedDate;
		this.mimeType = mimeType;
		this.file = new File(pathToFile);
		this.numPages = numPages;

		if (numPages.isPresent()) {
			for (int i = 1; i <= numPages.get(); i++) {
				pages.put(i, new Page(i));
			}
		}
	}

	@Override
	public File getFile() {
		return file;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getAuthor() {
		return author;
	}

	@Override
	public String getMimeType() {
		return mimeType;
	}

	@Override
	public LocalDate getLastModifiedDate() {
		return modifiedDate;
	}

	@Override
	public Optional<Integer> getNumberOfPages() {
		return numPages;
	}

	@Override
	public int getLastPageVisited() {
		return lastPageVisited;
	}

	@Override
	public List<Integer> getBookmarks() {
		List<Integer> bookmarks = new ArrayList<>();
		for (Page p : pages.values()) {
			if (p.isBookmarked()) {
				bookmarks.add(p.getPageNum());
			}
		}
		return bookmarks;
	}

	@Override
	public void toggleBookmark(int pageNum) {
		Page page = pages.get(pageNum);
		if (page != null) {
			page.toggleBookmark();
			emitEvent(new ToggleBookmarkEvent(this, pageNum, page.isBookmarked()));
		}
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void setAuthor(String author) {
		this.author = author;
	}

	@Override
	public void setLastPageVisited(int lastPageVisited) {
		this.lastPageVisited = lastPageVisited;
	}

	@Override
	public void addAnnotation(int pageNum, String text) {
		Page page = pages.get(pageNum);
		if (page != null) {
			page.addAnnotation(text);
			int annotationNum = page.getAnnotationCount(); // última adicionada
			emitEvent(new AddAnnotationEvent(this, pageNum, annotationNum, text, page.hasAnnotations()));
		}
	}

	@Override
	public void removeAnnotation(int pageNum, int annotNum) {
		Page page = pages.get(pageNum);
		if (page != null) {
			page.removeAnnotation(annotNum);
			emitEvent(new RemoveAnnotationEvent(this, pageNum, annotNum, page.hasAnnotations()));
		}
	}

	@Override
	public int numberOfAnnotations(int pageNum) {
		Page page = pages.get(pageNum);
		return (page != null) ? page.getAnnotationCount() : 0;
	}

	@Override
	public Iterable<String> getAnnotations(int pageNum) {
		Page page = pages.get(pageNum);
		if (page == null) return Collections.emptyList();
		List<String> list = new ArrayList<>();
		for (Annotation a : page.getAnnotations()) {
			list.add(a.getAnnotationText());
		}
		return list;
	}

	@Override
	public String getAnnotationText(int pageNum, int annotNum) {
		Page page = pages.get(pageNum);
		return (page != null) ? page.getAnnotationText(annotNum) : null;
	}

	@Override
	public boolean hasAnnotations(int pageNum) {
		Page page = pages.get(pageNum);
		return page != null && page.hasAnnotations();
	}

	@Override
	public boolean isBookmarked(int pageNum) {
		Page page = pages.get(pageNum);
		return page != null && page.isBookmarked();
	}

	@Override
	public boolean isBookmarked() {
		return !getBookmarks().isEmpty();
	}

	@Override
	public boolean matches(String regexp) {
		return title.matches(regexp) || author.matches(regexp);
	}

	@Override
	public int compareTo(IDocument other) {
		return this.file.compareTo(other.getFile());
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof IDocument)) return false;
		IDocument other = (IDocument) o;
		return this.file.equals(other.getFile());
	}

	@Override
	public int hashCode() {
		return file.hashCode();
	}
}
