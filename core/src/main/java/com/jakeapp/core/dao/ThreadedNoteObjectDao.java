package com.jakeapp.core.dao;
import java.util.List;
import java.util.UUID;
import com.jakeapp.core.dao.exceptions.NoSuchJakeObjectException;
import com.jakeapp.core.domain.NoteObject;
import com.jakeapp.core.domain.Tag;

public class ThreadedNoteObjectDao implements INoteObjectDao {

	private INoteObjectDao dao;

	public ThreadedNoteObjectDao(INoteObjectDao dao) {
		this.dao = dao;
	}

	// This file was automatically generated by generateDao.sh. Do not modify. 

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public NoteObject persist(final NoteObject noin) {
		
		try { 
			return SpringThreadBroker.getInstance().doTask(new InjectableTask<NoteObject>() {

				@Override
				public NoteObject calculate() throws Exception {
					return ThreadedNoteObjectDao.this.dao.persist(noin);
				}
			});
		}catch(RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public NoteObject get(final UUID objectId) throws NoSuchJakeObjectException {
		
		try { 
			return SpringThreadBroker.getInstance().doTask(new InjectableTask<NoteObject>() {

				@Override
				public NoteObject calculate() throws Exception {
					return ThreadedNoteObjectDao.this.dao.get(objectId);
				}
			});
		}catch(RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public List<NoteObject> getAll() {
		
		try { 
			return SpringThreadBroker.getInstance().doTask(new InjectableTask<List<NoteObject>>() {

				@Override
				public List<NoteObject> calculate() throws Exception {
					return ThreadedNoteObjectDao.this.dao.getAll();
				}
			});
		}catch(RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void delete(final NoteObject jakeObject) {
		
		try { 
			SpringThreadBroker.getInstance().doTask(new InjectableTask<Void>() {

				@Override
				public Void calculate() throws Exception {
					ThreadedNoteObjectDao.this.dao.delete(jakeObject);
					return null;
				}
			});
		}catch(RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	
	}

	/**
	 * {@inheritDoc}
	 */	
	@Override
	public NoteObject complete(final NoteObject jakeObject) throws NoSuchJakeObjectException {
		
		try { 
			return SpringThreadBroker.getInstance().doTask(new InjectableTask<NoteObject>() {

				@Override
				public NoteObject calculate() throws Exception {
					return ThreadedNoteObjectDao.this.dao.complete(jakeObject);
				}
			});
		}catch(RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	
	}


}