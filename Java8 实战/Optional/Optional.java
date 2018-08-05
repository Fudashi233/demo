private static void optional() {

    Map<String, String> map = new HashMap<>();
    map.put("a", "5");
    map.put("b", "true");
    map.put("c", "-3");

    String key = "c";
    Integer result = Optional.ofNullable(map.get(key))
            .flatMap(val -> parseToInt(val))
            .filter(val -> val > 0)
            .orElse(0);
    System.out.println(result);
}

private static Optional<Integer> parseToInt(String str) {

    Objects.requireNonNull(str, "str is null");
    try {
        return Optional.of(Integer.parseInt(str));
    } catch (NumberFormatException e) {
        return Optional.empty();
    }
}