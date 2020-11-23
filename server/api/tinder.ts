// Minimal Tinder Setup

type Profile = {
	id: string,
	name: string,
	about: string,
	avatars: string[],
	likes: string[]
}

let Data: { [key: string]: Profile } = {
    "0": {
        id: "0",
        name: "Alice",
        about: "Message from Alice.",
        avatars: [],
        likes: []
    },
    "1": {
        id: "1",
        name: "Bob",
        about: "Message from Bob.",
        avatars: [],
        likes: []
    }
}

type Message = {
    sender: string,
    msg: string
}

let Chats: { [key: string]: Message[] } = {}

let global_id_count = 2;

export function talk(a: string, b: string, msg: string) {
    let key = [parseInt(a), parseInt(b)].sort().join(",")
    if (!Chats[key]) {
        Chats[key] = []
    }
    Chats[key].push({
        sender: a,
        msg
    })
}

export function getChats(a: string, b: string) {
    let key = [parseInt(a), parseInt(b)].sort().join(",")
    if (!Chats[key]) {
        Chats[key] = []
    }
    return Chats[key]
}

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
