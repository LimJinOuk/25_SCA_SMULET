select distinct user.name as UserName,
       timetable.id as TimeTableId,
       course.major_or_culture ,course.name, course.credit , course.identify_number_of_course as 학수번호,
       scheduleOfCourse.day , scheduleOfCourse.timeStart , scheduleOfCourse.timeEnd,
       professor.name
from user
join timetable on user.id = timetable.userId
join timetableCourse on timetable.id = timetableCourse.timetableId
join course on timetableCourse.courseId = course.id
join scheduleOfCourse on course.schedule = scheduleOfCourse.id
join professor on course.professorName = professor.name
where user.id = 1;