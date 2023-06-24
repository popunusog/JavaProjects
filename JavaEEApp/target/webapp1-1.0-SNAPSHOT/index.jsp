
<!DOCTYPE html>
<html>
<head>
    <title>Alkoholowy24</title>
    <style>
        body {
            text-align: center;
        }

        h1 {
            margin-top: 50px;
            font-size: 24px;
        }

        .normal-text {
            color: blue;
        }

        .flashing-text {
            color: red;
            animation: blink 1s infinite;
        }

        @keyframes blink {
            0% {
                opacity: 1;
            }
            50% {
                opacity: 0;
            }
            100% {
                opacity: 1;
            }
        }

        form {
            display: inline-block;
            text-align: left;
            margin-top: 50px;
        }

        form input[type="number"],
        form input[type="submit"] {
            margin-bottom: 10px;
        }

        table {
            margin-top: 50px;
            margin-bottom: 50px;
            margin-left: auto;
            margin-right: auto;
            border-collapse: collapse;
        }

        th, td {
            padding: 10px 20px;
            border: 1px solid black;
        }

        th {
            background-color: orange;
        }

        img {
            width: 200px;
            height: 200px;
        }
    </style>

</head>
<body>
<span class="normal-text" style="font-size: 24px;">Witaj w dyskoncie alkoholowym! </span>
<span class="flashing-text" style="font-size: 24px;">24h</span>

<jsp:include page="/hello-servlet" />
</body>
</html>