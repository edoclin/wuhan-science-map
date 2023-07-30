import instance from "./ajax";

export const  hallDetailById = (id: string) => instance.get(`/baseHall/app/detail/${id}`)