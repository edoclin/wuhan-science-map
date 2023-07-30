import instance from "./ajax";

export const  courseDetailById = (id: string) => instance.get(`/baseCourse/app/detail/${id}`)


export const  registerCourseById = (id: string) => instance.get(`/userRegisterCourse/app/register/${id}`)
export const  favorCourseById = (id: string) => instance.get(`/userFavoriteCourse/app/favor/${id}`)
export const  wonderfulCourse = () => instance.get(`/baseCourse/app/wonderful`)