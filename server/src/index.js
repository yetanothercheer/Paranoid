const Koa = require("koa");
const app = new Koa;

const PORT = process.env.PORT || 7000;

[
    // TODO: NOT WORK ?
    async (ctx, next) => {
        Object.assign(ctx, { prologue: { a: 1} })
        console.log("==> " + ctx.prologue)
        await next()
    },

    // Logger
    require("koa-logger")(),

    // Error handler
    require("./handler")(),

    // Enable CORS
    require("@koa/cors")(),

    // Bodyparser
    require("koa-bodyparser")({ onerror: (err, ctx) => ctx.throw(499, "Invalid request body") }),

    // Router
    require("./route")(),

    // Last 404
    ctx => {
        console.log(ctx.prologue)
        ctx.throw("404 Not found", 404)
    }
].forEach(f => app.use(f))

app.listen(PORT)
