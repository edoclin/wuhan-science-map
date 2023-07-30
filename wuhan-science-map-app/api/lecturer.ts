import instance from "./ajax";

export const  lecturerDetailById = (id: string) => instance.get(`/lecturer/app/detail/${id}`)