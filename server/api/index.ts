import { NowRequest, NowResponse } from '@vercel/node'

import * as tinder from './tinder'

export default (request: NowRequest, response: NowResponse) => {
	response.setHeader('Access-Control-Allow-Origin', '*')
	response.setHeader("Access-Control-Allow-Credentials", "true");
	response.setHeader("Access-Control-Allow-Methods", "*");
	response.setHeader("Access-Control-Allow-Headers", "Content-Type,Access-Token");
	response.setHeader("Access-Control-Expose-Headers", "*");

	let reply = (message: any) => {
		response.status(200).send(message)
	}

	if (request.method !== "POST") { reply("Please use POST."); return; }

	let data = request.body
	if (data.f) {
		if (data.f === "register") {
			reply(tinder.register(data.name))
		} else if (data.f === "rename") {
			tinder.rename(data.id, data.name)
			reply("rename")
		} else if (data.f === "reabout") {
			tinder.reabout(data.id, data.about)
			reply("reabout")
		} else if (data.f === "addAvatar") {
			tinder.addAvatar(data.id, data.avator)
			reply("addAvatar")
		} else if (data.f === "check") {
			reply(tinder.check(data.a, data.b))
		} else if (data.f === "like") {
			tinder.like(data.a, data.b)
			reply("like")
		} else if (data.f === "get") {
			if (data.id) {
				reply(tinder.getProfileByID(data.id))
			} else {
				reply(tinder.getProfile())
			}
		} else if (data.f === "talk") {
			tinder.talk(data.a, data.b, data.message)
			reply("talk")
		} else if (data.f === "getChats") {
			reply(tinder.getChats(data.a, data.b))
		}
		return
	}
	reply('This should not happen')
}
