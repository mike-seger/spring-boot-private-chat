package org.privatechat.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.privatechat.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	public User findByEmail(String email);
	public User findById(long id);

	@Query(" FROM User u WHERE u.email IS NOT :excludedUser")
	public List<User> findFriendsListFor(@Param("excludedUser") String excludedUser);
}