package ru.gb;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Configuration configuration = new Configuration().configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            persistStudents(sessionFactory);
            System.out.println(getAllResultList(sessionFactory));
            mergeStudent(sessionFactory, 2L);
            deleteStudent(sessionFactory, 3L);
            Student byIdOfStudent = findByIdOfStudent(sessionFactory, 5L);
            System.out.println(byIdOfStudent);
            System.out.println(getAllResultList(sessionFactory));
            System.out.println(getMore20AgeList(sessionFactory, 20L));
//            Hibernate: drop table if exists student cascade
//            Hibernate: create table student (age bigint, id bigint not null, first_name varchar(255), second_name varchar(255), primary key (id))
//                    Hibernate: insert into student (age,first_name,second_name,id) values (?,?,?,?)
//            Hibernate: insert into student (age,first_name,second_name,id) values (?,?,?,?)
//            Hibernate: insert into student (age,first_name,second_name,id) values (?,?,?,?)
//            Hibernate: insert into student (age,first_name,second_name,id) values (?,?,?,?)
//            Hibernate: insert into student (age,first_name,second_name,id) values (?,?,?,?)
//            Hibernate: select s1_0.id,s1_0.age,s1_0.first_name,s1_0.second_name from student s1_0
//                    [Student{id=1, firstName='firstName1', secondName='secondName1', age=35}, Student{id=2, firstName='firstName2', secondName='secondName2', age=10}, Student{id=3, firstName='firstName3', secondName='secondName3', age=16}, Student{id=4, firstName='firstName4', secondName='secondName4', age=49}, Student{id=5, firstName='firstName5', secondName='secondName5', age=38}]
//            Hibernate: select s1_0.id,s1_0.age,s1_0.first_name,s1_0.second_name from student s1_0 where s1_0.id=?
//            Hibernate: update student set age=?,first_name=?,second_name=? where id=?
//            Hibernate: select s1_0.id,s1_0.age,s1_0.first_name,s1_0.second_name from student s1_0 where s1_0.id=?
//            Hibernate: delete from student where id=?
//            Hibernate: select s1_0.id,s1_0.age,s1_0.first_name,s1_0.second_name from student s1_0 where s1_0.id=?
//            Student{id=5, firstName='firstName5', secondName='secondName5', age=38}
//            Hibernate: select s1_0.id,s1_0.age,s1_0.first_name,s1_0.second_name from student s1_0
//                    [Student{id=1, firstName='firstName1', secondName='secondName1', age=35}, Student{id=2, firstName='updateName', secondName='secondName2', age=10}, Student{id=4, firstName='firstName4', secondName='secondName4', age=49}, Student{id=5, firstName='firstName5', secondName='secondName5', age=38}]
//            Hibernate: select s1_0.id,s1_0.age,s1_0.first_name,s1_0.second_name from student s1_0 where s1_0.age>20
//                    [Student{id=1, firstName='firstName1', secondName='secondName1', age=35}, Student{id=4, firstName='firstName4', secondName='secondName4', age=49}, Student{id=5, firstName='firstName5', secondName='secondName5', age=38}]
//            Hibernate: drop table if exists student cascade
        }
    }

    private static List<Student> getMore20AgeList(SessionFactory sessionFactory, Long age) {
        try (Session session = sessionFactory.openSession()) {
            String sql = "select student from Student student where student.age >" + age;
            Query<Student> query = session.createQuery(sql, Student.class);
            return query.getResultList();
        }
    }

    private static List<Student> getAllResultList(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            Query<Student> query = session.createQuery("select student from Student student", Student.class);
            return query.getResultList();
        }
    }

    private static Student findByIdOfStudent(SessionFactory sessionFactory, Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Student.class, id);
        }
    }

    private static void deleteStudent(SessionFactory sessionFactory, Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Student student = session.find(Student.class, id);
            session.remove(student);
            transaction.commit();
        }
    }

    private static void mergeStudent(SessionFactory sessionFactory, Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Student student = session.find(Student.class, id);
            student.setFirstName("updateName");
            session.merge(student);
            transaction.commit();
        }
    }

    private static void persistStudents(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            for (long i = 1; i <= 5; i++) {
                Student student = new Student(i, "firstName" + i, "secondName" + i, (long) new Random().nextInt(50));
                session.persist(student);
            }
            transaction.commit();
        }
    }
}
