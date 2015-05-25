package urujdas.service;

import urujdas.model.News;

import java.util.List;

public interface NewsService {

    List<News> getLatestAll(int latestCount);

    List<News> getAllFromId(long id, int count);

    List<News> getLatestByUser(int count);

    List<News> getByUserFromId(Long id, int count);

    List<News> getLatestBySubscription(int count);

    List<News> getBySubscriptionFromId(Long id, int count);

    List<News> getLatestFavourites(int count);

    List<News> getFavouritesFromId(Long id, int count);

    void create(News news);

    void addNewsToFavourites(Long newsId);

    void subscribe(Long userId);
}
