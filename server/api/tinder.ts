// Minimal Tinder Setup

type Profile = {
	id: string,
	name: string,
	about: string,
	avatars: string[],
	likes: string[]
}

let Data: { [key: string]: Profile } = {}
let global_id_count = 0;

export function register(_name: string) {
    let p: Profile = {
        id: (global_id_count++).toString(),
        name: _name,
        about: "",
        avatars: [],
        likes: []
    };
    Data[p.id] = p
    return p
}

export const rename = (_id: string, _name: string) => Data[_id].name = _name
export const reabout = (_id: string, _about: string) => Data[_id].about = _about
export const addAvatar = (_id: string, _avator: string) => Data[_id].avatars.push(_avator)

export const getProfile = () => {
    let choosed = Math.floor(Math.random() * (global_id_count))
    return choosed === global_id_count ? Data[choosed - 1] : Data[choosed]
}

export const getProfileByID = (_id: string) => Data[_id]

export const like = (_id: string, _id2: string) => Data[_id].likes.push(_id2)
export const check = (_id: string, _id2: string) => Data[_id].likes.includes(_id2) && Data[_id2].likes.includes(_id)
