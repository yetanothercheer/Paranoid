const Router = require("@koa/router")

const router = new Router;

const BOOT_DATE = new Date();

// Wow!!! https://fossheim.io/writing/posts/css-text-gradient/
router.get("/", ctx => ctx.body = `
<style>
html {background-color: black; user-select: none}
</style>
<div style="margin-top: -55px; width: 99vw; height: 95vh; display: flex; flex-direction: column; align-items: center; justify-content: center;
    background-color: #f3ec78;
    background-image: linear-gradient(45deg, #f3ec78, #af4261);
    background-size: 100%;
    -webkit-background-clip: text;
    -moz-background-clip: text;
    -webkit-text-fill-color: transparent; 
    -moz-text-fill-color: transparent;
">
<h1 style='font-family: mono; font-size: 10em;'>Paranoid API Endpoint</h1>
<h1>Hosted since <span style='font-size: 1.3em; font-weight: 300'>${BOOT_DATE}</span></h1>
</div>
`)

let id_index = 2;

let users = {
    "0": { id: "0", name: "Bootstrap User Alice" },
    "1": { id: "1", name: "Bootstrap User Bob" }
}

router.get("/user", ctx => ctx.body = Object.values(users))

router.get("/user/:id", ctx => ctx.body = users[ctx.params.id] || ctx.throw("User not exist"))

router.post("/user", ctx => {
    let body = ctx.request.body
    switch (body.action) {
        case 'CREATE':
            let id = id_index++
            let user = { id, name: `Unspecified #${id}` }
            users[id] = user
            ctx.body = user
            break;
        case 'LIKE':
            let { from, to } = body
            if (!users[from].likes) users[from].likes = []
            users[from].likes.push(to)
            ctx.body = { message: "OK" }
            break;
        case 'UPDATE':
            ctx.throw("Not supported at /user (BY NOW)");
            break;
        default:
            ctx.throw("Not supported at /user");
            break;
    }
})

let chats = {}

router.get("/chat/:room_id", ctx => ctx.body = chats[ctx.params.room_id] || [])
router.post("/chat/:room_id", ctx => {
    let room_id = ctx.params.room_id;
    if (!chats[room_id]) chats[room_id] = [];
    chats[room_id].push(ctx.request.body)
    ctx.body = { message: "Roger." }
})

module.exports = () => router.routes();
