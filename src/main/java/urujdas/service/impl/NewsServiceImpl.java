package urujdas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import urujdas.dao.NewsDao;
import urujdas.dao.SubscriptionDao;
import urujdas.model.News;
import urujdas.model.Subscription;
import urujdas.model.User;
import urujdas.service.NewsService;
import urujdas.service.UserService;
import urujdas.util.Validation;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class NewsServiceImpl implements NewsService {

    @Autowired
    private UserService userService;

    @Autowired
    private NewsDao newsDao;

    @Autowired
    private SubscriptionDao subscriptionDao;

    @Override
    public List<News> getLatestAll(int latestCount) {
        Validation.isGreaterThanZero(latestCount);

        return newsDao.getLatestAll(latestCount);
    }

    @Override
    public List<News> getAllFromId(long id, int count) {
        Validation.isGreaterThanZero(id);
        Validation.isGreaterThanZero(count);

        return newsDao.getAllFromId(id, count);
    }

    @Override
    public List<News> getLatestByUser(int count) {
        Validation.isGreaterThanZero(count);

        User currentUser = userService.getCurrentUser();

        return newsDao.getLatestByUser(currentUser, count);
    }

    @Override
    public List<News> getByUserFromId(Long id, int count) {
        Validation.isGreaterThanZero(count);

        User currentUser = userService.getCurrentUser();

        return newsDao.getByUserFromId(currentUser, id, count);
    }

    @Override
    public List<News> getLatestBySubscription(int count) {
        Validation.isGreaterThanZero(count);

        User currentUser = userService.getCurrentUser();
        Subscription subscription = subscriptionDao.getByUser(currentUser);

        return newsDao.getLatestBySubscription(subscription, count);
    }

    @Override
    public List<News> getBySubscriptionFromId(Long id, int count) {
        Validation.isGreaterThanZero(id);
        Validation.isGreaterThanZero(count);

        User currentUser = userService.getCurrentUser();
        Subscription subscription = subscriptionDao.getByUser(currentUser);

        return newsDao.getBySubscriptionFromId(subscription, id, count);
    }

    @Override
    public List<News> getLatestFavourites(int count) {
        Validation.isGreaterThanZero(count);

        User currentUser = userService.getCurrentUser();

        return newsDao.getLatestFavourites(currentUser, count);
    }

    @Override
    public List<News> getFavouritesFromId(Long id, int count) {
        Validation.isGreaterThanZero(id);
        Validation.isGreaterThanZero(count);

        User currentUser = userService.getCurrentUser();

        return newsDao.getFavouritesFromId(currentUser, id, count);
    }

    @Override
    @Transactional(readOnly = false)
    public void create(News news) {
        User currentUser = userService.getCurrentUser();

        news = News.fromNews(news)
                .withCreationDate(LocalDateTime.now(Clock.systemUTC()))
                .withAuthor(currentUser)
                .build();

        newsDao.create(news);
    }

    @Override
    @Transactional(readOnly = false)
    public void addNewsToFavourites(Long newsId) {
        User currentUser = userService.getCurrentUser();

        News news = newsDao.getById(newsId);

        newsDao.addToFavourites(currentUser, news);
    }

    @Override
    @Transactional(readOnly = false)
    public void subscribe(Long userId) {
        User currentUser = userService.getCurrentUser();
        User author = userService.getById(userId);

        subscriptionDao.create(currentUser, author);
    }
}
