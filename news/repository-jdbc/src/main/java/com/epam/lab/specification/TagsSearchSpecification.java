package com.epam.lab.specification;

import java.util.List;

import static com.epam.lab.repository.DAOConstants.QUOTATION_MARK;
import static com.epam.lab.repository.DAOConstants.QUOTATION_MARK_AND_COLUMN;

public class TagsSearchSpecification implements SearchSpecification {

    private List<String> tagName;

    public TagsSearchSpecification(List<String> tagName) {
        this.tagName = tagName;
    }

    @Override
    public String getSql() {
        StringBuilder sb = new StringBuilder();
        if (tagName.size() == 1) {
            sb.append(QUOTATION_MARK);
            sb.append(tagName.get(0));
            sb.append(QUOTATION_MARK);
        } else {
            for (String oneTagName : tagName) {
                sb.append(QUOTATION_MARK);
                sb.append(oneTagName);
                sb.append(QUOTATION_MARK_AND_COLUMN);
            }
            sb.delete(sb.length() - 1, sb.length());
        }

        return " AND news.id IN (SELECT news_id FROM news_tag WHERE tag_id IN (SELECT id FROM tag " +
                " WHERE name IN (" + sb.toString() + ")) GROUP BY news_id HAVING COUNT(news_id) >= " +
                tagName.size() + ") ";
    }
}
