INSERT INTO mocks (URI, RESPONSE)
VALUES ('/test', '{"code":200}'),
       ('/manual',
        '{"code":200,"headers":[{"key":"Content-Type","value":"application/json"},{"key":"Transfer-Encoding","value":"chunked"},{"key":"Date","value":"Tue, 10 Sep 2024 10:57:27 GMT"},{"key":"Keep-Alive","value":"timeout=60"},{"key":"Connection","value":"keep-alive"}],"cookies":[],"body":"{\n\"result\":\"0\"\n}"}'),
       ('/manual/error',
        '{"code":500,"headers":[{"key":"Content-Type","value":"application/json"},{"key":"Transfer-Encoding","value":"chunked"},{"key":"Date","value":"Tue, 10 Sep 2024 10:57:27 GMT"},{"key":"Keep-Alive","value":"timeout=60"},{"key":"Connection","value":"keep-alive"}],"cookies":[],"body":"{\n\"result\":\"1\"\n}"}')
;