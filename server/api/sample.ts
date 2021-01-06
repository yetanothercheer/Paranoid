/**
 * SAMPLE RESTFUL API
 */

import { NowRequest, NowResponse } from '@vercel/node'
import fetch from 'node-fetch'

// Make life easier

const _plain_match = (v: any, dict: any) => dict[v] || dict._
const _safe_call = (f: any) => f && f()
const match = (v: any, dict: any) => _safe_call(_plain_match(v, dict))

const urlMatch = async (v: any, dict: any) => {
    let urls = []
    Object.keys(dict).forEach(k => {
        urls.push({
            weight: (k.match(/\//g) || []).length,
            key: k,
            f: dict[k]
        })
    })
    // sort by descent order
    urls.sort((a, b) => b.weight - a.weight)
    console.log("")
    for (let url of urls) {
        let transformed_regex = url.key.replace(/\[(.*)\]/, "(.*)")
        // console.log(transformed_regex)
        let result = v.match(
            RegExp(transformed_regex)
        )
        if (result) {
            console.log(`====> ${url.key}`)
            await url.f(result[1])
            return
        } else {
            console.log(`<==== ${url.key}`)
        }
    }
}

const GET = async uri => await fetch(uri)
const POST = async (uri, json) =>
    await fetch(uri, {
        method: "POST",
        body: JSON.stringify(json),
        headers: { 'Content-Type': 'application/json' }
    })
const PUT = async (uri, json) =>
    await fetch(uri, {
        method: "PUT",
        body: JSON.stringify(json),
        headers: { 'Content-Type': 'application/json' }
    })

// JSON Stroage API

const JSONSTROAGE = "https://jsonstorage.net/api/items"

async function get(id) {
    let res = await GET(`${JSONSTROAGE}/${id}`)
    return _plain_match(res.status, {
        200: await res.json(),
        _: {}
    })
}

async function create(json) {
    let res = await POST(`${JSONSTROAGE}`, json)
    return _plain_match(res.status, {
        201: ((await res.json()).uri as String).split("/").pop(),
        _: ""
    })
}

async function update(id, json) {
    let res = await PUT(`${JSONSTROAGE}/${id}`, json)
    return _plain_match(res.status, {
        200: await res.json(),
        _: ""
    })
}

// Data Definitions

type User = {
    id: string
    name: string
}

type UserDatabase = { string: User }

let db_id = "a3312091-e65d-4c90-8ea7-cffccfb782bd"

export default async (request: NowRequest, response: NowResponse) => {
    response.setHeader('Access-Control-Allow-Origin', '*')
    response.setHeader("Access-Control-Allow-Credentials", "true");
    response.setHeader("Access-Control-Allow-Methods", "*");
    response.setHeader("Access-Control-Allow-Headers", "Content-Type,Access-Token");
    response.setHeader("Access-Control-Expose-Headers", "*");

    await urlMatch(request.url, {
        "/sample": () => {
            match(request.method, {
                'GET': async () => {
                    let db = await get(db_id) as UserDatabase
                    response.status(200).send(db)
                },
                'POST': async () => {
                    let db = await get(db_id) as UserDatabase
                    let user = request.body as User
                    user.id = user.name
                    db[user.id] = user
                    await update(db_id, db)
                    response.status(200).send({ message: "user created" })
                },
                _: () => {
                    console.log(`Not support: ${request.method}`)
                    response.status(200).send({ message: `Not support: ${request.method}` })
                }
            })
        },
        "/sample/[id]": (id) => {
            match(request.method, {
                'GET': async () => {
                    let db = await get(db_id) as UserDatabase
                    response.status(200).send(db[id])
                },
                'PUT': async () => {
                    let db = await get(db_id) as UserDatabase
                    let user = request.body as User
                    if (db[user.id] !== null) {
                        user.id = user.name
                        db[user.id] = user
                        await update(db_id, db)
                        response.status(200).send({ message: "user updated" })
                    } else {
                        response.status(300).send({ message: "user not exist" })
                    }
                },
                _: () => {
                    console.log(`Not support: ${request.method}`)
                    response.status(200).send({ message: `Not support: ${request.method}` })
                }
            })
        }
    })
}
