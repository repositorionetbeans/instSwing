/*
 * Copyright 2019 Windows 10.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;

/**
 *
 * @author Windows 10
 */
public class InstagramUserSummaryJpaController implements Serializable {
    
    public InstagramUserSummaryJpaController() {
        this.emf = Persistence.createEntityManagerFactory("org.brunocvcunha.instagram4j_instagram4j_jar_1.11-SNAPSHOTPU");
    }

    public InstagramUserSummaryJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(InstagramUserSummary instagramUserSummary) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(instagramUserSummary);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findInstagramUserSummary(instagramUserSummary.getPk()) != null) {
                throw new PreexistingEntityException("InstagramUserSummary " + instagramUserSummary + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(InstagramUserSummary instagramUserSummary) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            instagramUserSummary = em.merge(instagramUserSummary);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                long id = instagramUserSummary.getPk();
                if (findInstagramUserSummary(id) == null) {
                    throw new NonexistentEntityException("The instagramUserSummary with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            InstagramUserSummary instagramUserSummary;
            try {
                instagramUserSummary = em.getReference(InstagramUserSummary.class, id);
                instagramUserSummary.getPk();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The instagramUserSummary with id " + id + " no longer exists.", enfe);
            }
            em.remove(instagramUserSummary);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<InstagramUserSummary> findInstagramUserSummaryEntities() {
        return findInstagramUserSummaryEntities(true, -1, -1);
    }

    public List<InstagramUserSummary> findInstagramUserSummaryEntities(int maxResults, int firstResult) {
        return findInstagramUserSummaryEntities(false, maxResults, firstResult);
    }

    private List<InstagramUserSummary> findInstagramUserSummaryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(InstagramUserSummary.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public InstagramUserSummary findInstagramUserSummary(long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(InstagramUserSummary.class, id);
        } finally {
            em.close();
        }
    }

    public int getInstagramUserSummaryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<InstagramUserSummary> rt = cq.from(InstagramUserSummary.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
