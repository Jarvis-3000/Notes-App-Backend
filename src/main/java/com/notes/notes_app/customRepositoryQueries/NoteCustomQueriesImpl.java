package com.notes.notes_app.customRepositoryQueries;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.notes.notes_app.model.Note;


@Service
public class NoteCustomQueriesImpl implements NoteCustomQueries {
  @Autowired
  private MongoTemplate mongoTemplate;

  @Override
  public boolean existByTitleContainingIgnoreCase( String title) {
    Query query = new Query();
    query.addCriteria(Criteria.where("title").regex(title, "i"));

    return mongoTemplate.exists(query, Note.class);
  }

  @Override
  public List<Note> findByTitleContainingIgnoreCase(String titleSubString) {
    Query query = new Query();
    query.addCriteria(Criteria.where("title").regex(titleSubString, "i"));

    return mongoTemplate.find(query, Note.class);
  }

  @Override
  public List<Note> findByDescriptionContainingIgnoreCase(String descriptionSubString) {
    Criteria criteria = Criteria.where("description").regex(descriptionSubString, "i");
    Query query = new Query(criteria);

    return mongoTemplate.find(query, Note.class);
  }

  @Override
  public List<Note> findByCreatedDateRange(LocalDate startDate, LocalDate endDate) {
    LocalDateTime startDateTime = startDate.atStartOfDay();
    LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

    Criteria criteria = Criteria.where("createdAt").gte(startDateTime).lte(endDateTime);
    Query query = new Query(criteria);

    return mongoTemplate.find(query, Note.class);
  }

}
