FROM ruby:3.1
COPY ./Gemfile /build/
WORKDIR /build/
RUN gem install bundle
RUN bundle install
